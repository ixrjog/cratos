package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.converter.EdsIdentityConverter;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsLdapIdentityExtension;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.repo.LdapGroupRepo;
import com.baiyi.cratos.eds.ldap.repo.LdapPersonRepo;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.context.EdsIdentityExtensionContext;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.LDAP_USER_GROUPS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/27 11:03
 * &#064;Version 1.0
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class EdsLdapIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsLdapIdentityExtension {

    private final LdapPersonRepo ldapPersonRepo;
    private final LdapGroupRepo ldapGroupRepo;

    public EdsLdapIdentityExtensionImpl(EdsIdentityExtensionContext context, LdapPersonRepo ldapPersonRepo,
                                        LdapGroupRepo ldapGroupRepo) {
        super(context);
        this.ldapPersonRepo = ldapPersonRepo;
        this.ldapGroupRepo = ldapGroupRepo;
    }

    @Override
    public EdsIdentityVO.LdapIdentityDetails queryLdapIdentityDetails(
            EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails) {
        String username = queryLdapIdentityDetails.getUsername();
        User user = context.getUserService()
                .getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.LdapIdentityDetails.NO_DATA;
        }
        List<EdsAsset> assets = onlyInTheInstance(
                context.getEdsAssetService()
                        .queryByTypeAndKey(EdsAssetTypeEnum.LDAP_PERSON.name(), username), queryLdapIdentityDetails
        );
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.LdapIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.LdapIdentity> ldapIdentities = assets.stream()
                .map(asset -> {
                    EdsAssetIndex index = context.getEdsAssetIndexService()
                            .getByAssetIdAndName(asset.getId(), LDAP_USER_GROUPS);
                    List<String> groups = Objects.nonNull(index) ? Splitter.on(";")
                            .splitToList(index.getValue()) : List.of();
                    return EdsIdentityVO.LdapIdentity.builder()
                            .username(username)
                            .user(context.getUserWrapper()
                                          .wrapToTarget(user))
                            .instance(context.getEdsInstanceWrapper()
                                              .wrapToTarget(context.getEdsInstanceService()
                                                                    .getById(asset.getInstanceId())))
                            .account(context.getEdsAssetWrapper()
                                             .wrapToTarget(asset))
                            .groups(groups)
                            .build();
                })
                .toList();
        return EdsIdentityVO.LdapIdentityDetails.builder()
                .username(username)
                .ldapIdentities(ldapIdentities)
                .build();
    }

    @Override
    public EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity) {
        EdsInstance instance = getEdsInstance(createLdapIdentity);
        User user = context.getUserService()
                .getByUsername(createLdapIdentity.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", createLdapIdentity.getUsername());
        }
        final String password = verifyAndGeneratePassword(createLdapIdentity.getPassword());
        // TODO 判断账户是否过期，锁定
        LdapPerson.Person person = EdsIdentityConverter.toLdapPerson(user);
        person.setUserPassword(password);
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person> holder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person>) context.getEdsProviderHolderFactory()
                    .createHolder(createLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            if (ldapPersonRepo.checkPersonInLdap(
                    holder.getInstance()
                            .getConfig(), person.getUsername()
            )) {
                // 身份已存在
                EdsIdentityException.runtime("The user already exists in the instance.");
            }
            ldapPersonRepo.create(
                    holder.getInstance()
                            .getConfig(), person
            );
            // 擦除密码
            person.setUserPassword(null);
            // 导入资产
            EdsAsset personAsset = holder.getProvider()
                    .importAsset(holder.getInstance(), person);
            return EdsIdentityVO.LdapIdentity.builder()
                    .username(createLdapIdentity.getUsername())
                    .password(password)
                    .user(context.getUserWrapper()
                                  .wrapToTarget(user))
                    .account(context.getEdsAssetWrapper()
                                     .wrapToTarget(personAsset))
                    .instance(context.getEdsInstanceWrapper()
                                      .wrapToTarget(instance))
                    .build();
        } catch (Exception ex) {
            throw new EdsIdentityException("Creating LDAP identity error: {}", ex.getMessage());
        }
    }

    @Override
    public EdsIdentityVO.LdapIdentity resetLdapUserPassword(
            EdsIdentityParam.ResetLdapUserPassword resetLdapUserPassword) {
        EdsInstance instance = getEdsInstance(resetLdapUserPassword);
        User user = context.getUserService()
                .getByUsername(resetLdapUserPassword.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", resetLdapUserPassword.getUsername());
        }
        final String password = verifyAndGeneratePassword(resetLdapUserPassword.getPassword());
        LdapPerson.Person person = EdsIdentityConverter.toLdapPerson(user);
        person.setUserPassword(password);
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person> holder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person>) context.getEdsProviderHolderFactory()
                    .createHolder(resetLdapUserPassword.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            if (!ldapPersonRepo.checkPersonInLdap(
                    holder.getInstance()
                            .getConfig(), resetLdapUserPassword.getUsername()
            )) {
                // 身份已存在
                EdsIdentityException.runtime("The user not exist in the instance.");
            }
            ldapPersonRepo.update(
                    holder.getInstance()
                            .getConfig(), person
            );
            return EdsIdentityVO.LdapIdentity.builder()
                    .username(resetLdapUserPassword.getUsername())
                    .password(password)
                    .user(context.getUserWrapper()
                                  .wrapToTarget(user))
                    //.asset(edsAssetWrapper.wrapToTarget(personAsset))
                    .instance(context.getEdsInstanceWrapper()
                                      .wrapToTarget(instance))
                    .build();
        } catch (Exception ex) {
            throw new EdsIdentityException("Reset LDAP user password error: {}", ex.getMessage());
        }
    }

    @Override
    public void deleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity) {
        EdsInstance instance = getEdsInstance(deleteLdapIdentity);
        User user = context.getUserService()
                .getByUsername(deleteLdapIdentity.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", deleteLdapIdentity.getUsername());
        }
        final String username = deleteLdapIdentity.getUsername();
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person> holder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person>) context.getEdsProviderHolderFactory()
                    .createHolder(deleteLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group>) context.getEdsProviderHolderFactory()
                    .createHolder(deleteLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            EdsConfigs.Ldap ldap = holder.getInstance()
                    .getConfig();
            if (!ldapPersonRepo.checkPersonInLdap(
                    holder.getInstance()
                            .getConfig(), deleteLdapIdentity.getUsername()
            )) {
                // 身份不存在
                return;
            }
            List<LdapGroup.Group> groups = ldapGroupRepo.searchGroupByUsername(ldap, username);
            // 删除用户的所有组权限
            if (!CollectionUtils.isEmpty(groups)) {
                groups.forEach(group -> {
                    ldapGroupRepo.removeGroupMember(ldap, group.getGroupName(), username);
                    // 重写Eds Ldap group资产
                    ldapGroupHolder.getProvider()
                            .importAsset(
                                    ldapGroupHolder.getInstance(),
                                    ldapGroupRepo.findGroup(ldap, group.getGroupName())
                            );
                });
            }
            // 删除Ldap user
            ldapPersonRepo.delete(ldap, username);
            // 删除资产
            context.getEdsAssetService()
                    .queryInstanceAssetByTypeAndKey(
                            deleteLdapIdentity.getInstanceId(),
                            EdsAssetTypeEnum.LDAP_PERSON.name(), username
                    )
                    .stream()
                    .mapToInt(EdsAsset::getId)
                    .forEach(context.getEdsFacade()::deleteEdsAssetById);
        } catch (Exception ex) {
            throw new EdsIdentityException("Delete LDAP identity error: {}", ex.getMessage());
        }
    }

    public void fixDeleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity) {
        EdsInstance instance = getEdsInstance(deleteLdapIdentity);
        User user = context.getUserService()
                .getByUsername(deleteLdapIdentity.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", deleteLdapIdentity.getUsername());
        }
        final String username = deleteLdapIdentity.getUsername();
        // 删除用户
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person> ldapPersonHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person>) context.getEdsProviderHolderFactory()
                    .createHolder(deleteLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            EdsConfigs.Ldap ldap = ldapPersonHolder.getInstance()
                    .getConfig();
            if (ldapPersonRepo.checkPersonInLdap(
                    ldapPersonHolder.getInstance()
                            .getConfig(), deleteLdapIdentity.getUsername()
            )) {
                // 删除Ldap user
                ldapPersonRepo.delete(ldap, username);
                // 删除资产
                context.getEdsAssetService()
                        .queryInstanceAssetByTypeAndKey(
                                deleteLdapIdentity.getInstanceId(),
                                EdsAssetTypeEnum.LDAP_PERSON.name(), username
                        )
                        .stream()
                        .mapToInt(EdsAsset::getId)
                        .forEach(context.getEdsFacade()::deleteEdsAssetById);
            }
        } catch (Exception ex) {
            throw new EdsIdentityException("Delete LDAP identity error: {}", ex.getMessage());
        }
        // 删除组
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group>) context.getEdsProviderHolderFactory()
                    .createHolder(deleteLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            EdsConfigs.Ldap ldap = ldapGroupHolder.getInstance()
                    .getConfig();
            List<LdapGroup.Group> groups = ldapGroupRepo.searchGroupByUsername(ldap, username);
            // 删除用户的所有组权限
            if (!CollectionUtils.isEmpty(groups)) {
                groups.forEach(group -> {
                    ldapGroupRepo.removeGroupMember(ldap, group.getGroupName(), username);
                    // 重写Eds Ldap group资产
                    ldapGroupHolder.getProvider()
                            .importAsset(
                                    ldapGroupHolder.getInstance(),
                                    ldapGroupRepo.findGroup(ldap, group.getGroupName())
                            );
                });
            }
        } catch (Exception ex) {
            throw new EdsIdentityException("Delete LDAP group identity error: {}", ex.getMessage());
        }
    }

    @Override
    public void addLdapUserToGroup(EdsIdentityParam.AddLdapUserToGroup addLdapUserToGroup) {
        EdsInstance instance = getEdsInstance(addLdapUserToGroup);
        User user = context.getUserService()
                .getByUsername(addLdapUserToGroup.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", addLdapUserToGroup.getUsername());
        }
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group>) context.getEdsProviderHolderFactory()
                    .createHolder(addLdapUserToGroup.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            final EdsConfigs.Ldap ldap = ldapGroupHolder.getInstance()
                    .getConfig();
            if (!ldapPersonRepo.checkPersonInLdap(
                    ldapGroupHolder.getInstance()
                            .getConfig(), addLdapUserToGroup.getUsername()
            )) {
                // 身份不存在
                EdsIdentityException.runtime("The user does not exist in LDAP instance.");
            }
            LdapGroup.Group group = ldapGroupRepo.findGroup(ldap, addLdapUserToGroup.getGroup());
            if (Objects.isNull(group)) {
                EdsIdentityException.runtime("LDAP group {} does not exist.", addLdapUserToGroup.getGroup());
            }
            // 新增组成员
            ldapGroupRepo.addGroupMember(ldap, addLdapUserToGroup.getGroup(), addLdapUserToGroup.getUsername());
            // 刷新数据
            postRefreshEdsData(ldap, addLdapUserToGroup, user, addLdapUserToGroup.getGroup(), ldapGroupHolder);
        } catch (Exception ex) {
            throw new EdsIdentityException("Add LDAP user to the group error: {}", ex.getMessage());
        }
    }

    @Override
    public void removeLdapUserFromGroup(EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup) {
        EdsInstance instance = getEdsInstance(removeLdapUserFromGroup);
        User user = context.getUserService()
                .getByUsername(removeLdapUserFromGroup.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", removeLdapUserFromGroup.getUsername());
        }
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group>) context.getEdsProviderHolderFactory()
                    .createHolder(removeLdapUserFromGroup.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            final EdsConfigs.Ldap ldap = ldapGroupHolder.getInstance()
                    .getConfig();
            if (!ldapPersonRepo.checkPersonInLdap(ldap, removeLdapUserFromGroup.getUsername())) {
                EdsIdentityException.runtime("The user does not exist in LDAP instance.");
            }
            LdapGroup.Group group = ldapGroupRepo.findGroup(ldap, removeLdapUserFromGroup.getGroup());
            if (Objects.isNull(group)) {
                EdsIdentityException.runtime("LDAP group {} does not exist.", removeLdapUserFromGroup.getGroup());
            }
            ldapGroupRepo.removeGroupMember(
                    ldap, removeLdapUserFromGroup.getGroup(),
                    removeLdapUserFromGroup.getUsername()
            );
            postRefreshEdsData(
                    ldap, removeLdapUserFromGroup, user, removeLdapUserFromGroup.getGroup(),
                    ldapGroupHolder
            );
        } catch (Exception ex) {
            throw new EdsIdentityException("Remove LDAP user from group error: {}", ex.getMessage());
        }
    }

    private void postRefreshEdsData(EdsConfigs.Ldap ldap, HasEdsInstanceId hasEdsInstanceId, User user,
                                    String groupName,
                                    EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group> ldapGroupHolder) {
        // 导入person资产（重写索引）
        LdapPerson.Person person = ldapPersonRepo.findPerson(ldap, EdsIdentityConverter.toLdapPerson(user));
        EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person> ldapPersonHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person>) context.getEdsProviderHolderFactory()
                .createHolder(hasEdsInstanceId.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
        ldapPersonHolder.getProvider()
                .importAsset(ldapGroupHolder.getInstance(), person);
        // 导入group资产（重写索引）
        ldapGroupHolder.getProvider()
                .importAsset(ldapGroupHolder.getInstance(), ldapGroupRepo.findGroup(ldap, groupName));
    }

    @Override
    public Set<String> queryLdapGroups(EdsIdentityParam.QueryLdapGroups queryLdapGroups) {
        getEdsInstance(queryLdapGroups);
        try {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapGroup.Group>) context.getEdsProviderHolderFactory()
                    .createHolder(queryLdapGroups.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            final EdsConfigs.Ldap ldap = ldapGroupHolder.getInstance()
                    .getConfig();
            List<LdapGroup.Group> groups = ldapGroupRepo.queryGroup(ldap);
            return groups.stream()
                    .map(LdapGroup.Group::getGroupName)
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            throw new EdsIdentityException("Query LDAP groups error: {}", ex.getMessage());
        }
    }

    private EdsInstance getEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        return getAndVerifyEdsInstance(hasEdsInstanceId, EdsInstanceTypeEnum.LDAP);
    }

}
