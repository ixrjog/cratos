package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.converter.EdsIdentityConverter;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.repo.LdapGroupRepo;
import com.baiyi.cratos.eds.ldap.repo.LdapPersonRepo;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.identity.extension.EdsLdapIdentityExtension;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/27 11:03
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class EdsLdapIdentityExtensionImpl implements EdsLdapIdentityExtension {

    private final EdsAssetWrapper edsAssetWrapper;
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceWrapper edsInstanceWrapper;
    private final UserService userService;
    private final UserWrapper userWrapper;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final LdapPersonRepo ldapPersonRepo;
    private final LdapGroupRepo ldapGroupRepo;
    private final EdsAssetService edsAssetService;
    private final EdsFacade edsFacade;

    @Override
    public EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity) {
        EdsInstance instance = getAndVerifyEdsInstance(createLdapIdentity);
        User user = userService.getByUsername(createLdapIdentity.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", createLdapIdentity.getUsername());
        }
        final String password = verifyAndGeneratePassword(createLdapIdentity.getPassword());
        // TODO 判断账户是否过期，锁定
        LdapPerson.Person person = EdsIdentityConverter.toLdapPerson(user);
        person.setUserPassword(password);
        try {
            EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapPerson.Person> holder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapPerson.Person>) holderBuilder.newHolder(
                    createLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            if (ldapPersonRepo.checkPersonInLdap(holder.getInstance()
                    .getEdsConfigModel(), person.getUsername())) {
                // 身份已存在
                EdsIdentityException.runtime("The user already exists in the instance.");
            }
            ldapPersonRepo.create(holder.getInstance()
                    .getEdsConfigModel(), person);
            // 导入资产
            EdsAsset personAsset = holder.getProvider()
                    .importAsset(holder.getInstance(), person);
            return EdsIdentityVO.LdapIdentity.builder()
                    .username(createLdapIdentity.getUsername())
                    .password(password)
                    .user(userWrapper.wrapToTarget(user))
                    .asset(edsAssetWrapper.wrapToTarget(personAsset))
                    .instance(edsInstanceWrapper.wrapToTarget(instance))
                    .build();
        } catch (Exception ex) {
            throw new EdsIdentityException("Creating LDAP identity error: {}", ex.getMessage());
        }
    }

    @Override
    public void deleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity) {
        EdsInstance instance = getAndVerifyEdsInstance(deleteLdapIdentity);
        User user = userService.getByUsername(deleteLdapIdentity.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", deleteLdapIdentity.getUsername());
        }
        final String username = deleteLdapIdentity.getUsername();
        try {
            EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapPerson.Person> holder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapPerson.Person>) holderBuilder.newHolder(
                    deleteLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group>) holderBuilder.newHolder(
                    deleteLdapIdentity.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            EdsLdapConfigModel.Ldap ldap = holder.getInstance()
                    .getEdsConfigModel();
            if (!ldapPersonRepo.checkPersonInLdap(holder.getInstance()
                    .getEdsConfigModel(), deleteLdapIdentity.getUsername())) {
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
                            .importAsset(ldapGroupHolder.getInstance(),
                                    ldapGroupRepo.findGroup(ldap, group.getGroupName()));
                });
            }
            // 删除Ldap user
            ldapPersonRepo.delete(ldap, username);
            edsAssetService.queryInstanceAssetByTypeAndKey(deleteLdapIdentity.getInstanceId(),
                            EdsAssetTypeEnum.LDAP_GROUP.name(), username)
                    .stream()
                    .mapToInt(EdsAsset::getId)
                    .forEach(edsFacade::deleteEdsAssetById);
        } catch (Exception ex) {
            throw new EdsIdentityException("Delete LDAP identity error: {}", ex.getMessage());
        }
    }

    @Override
    public void addLdapUserToTheGroup(EdsIdentityParam.AddLdapUserToTheGroup addLdapUserToTheGroup) {
        EdsInstance instance = getAndVerifyEdsInstance(addLdapUserToTheGroup);
        User user = userService.getByUsername(addLdapUserToTheGroup.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", addLdapUserToTheGroup.getUsername());
        }
        try {
            EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group>) holderBuilder.newHolder(
                    addLdapUserToTheGroup.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            final EdsLdapConfigModel.Ldap ldap = ldapGroupHolder.getInstance()
                    .getEdsConfigModel();
            if (!ldapPersonRepo.checkPersonInLdap(ldapGroupHolder.getInstance()
                    .getEdsConfigModel(), addLdapUserToTheGroup.getUsername())) {
                // 身份不存在
                EdsIdentityException.runtime("The user does not exist in LDAP instance.");
            }
            LdapGroup.Group group = ldapGroupRepo.findGroup(ldap, addLdapUserToTheGroup.getGroup());
            if (Objects.isNull(group)) {
                EdsIdentityException.runtime("LDAP group {} does not exist.", addLdapUserToTheGroup.getGroup());
            }
            // 新增组成员
            ldapGroupRepo.addGroupMember(ldap, addLdapUserToTheGroup.getGroup(), addLdapUserToTheGroup.getUsername());
            // 刷新数据
            postRefreshEdsData(ldap, addLdapUserToTheGroup, user, addLdapUserToTheGroup.getGroup(), ldapGroupHolder);
        } catch (Exception ex) {
            throw new EdsIdentityException("Add Ldap user to the group error: {}", ex.getMessage());
        }
    }

    @Override
    public void removeLdapUserFromGroup(EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup) {
        EdsInstance instance = edsInstanceService.getById(removeLdapUserFromGroup.getInstanceId());
        User user = userService.getByUsername(removeLdapUserFromGroup.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", removeLdapUserFromGroup.getUsername());
        }
        try {
            EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group>) holderBuilder.newHolder(
                    removeLdapUserFromGroup.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            final EdsLdapConfigModel.Ldap ldap = ldapGroupHolder.getInstance()
                    .getEdsConfigModel();
            if (!ldapPersonRepo.checkPersonInLdap(ldapGroupHolder.getInstance()
                    .getEdsConfigModel(), removeLdapUserFromGroup.getUsername())) {
                // 身份不存在
                EdsIdentityException.runtime("The user does not exist in LDAP instance.");
            }
            LdapGroup.Group group = ldapGroupRepo.findGroup(ldap, removeLdapUserFromGroup.getGroup());
            if (Objects.isNull(group)) {
                EdsIdentityException.runtime("LDAP group {} does not exist.", removeLdapUserFromGroup.getGroup());
            }
            // 移除组成员
            ldapGroupRepo.removeGroupMember(ldap, removeLdapUserFromGroup.getGroup(),
                    removeLdapUserFromGroup.getUsername());
            // 刷新数据
            postRefreshEdsData(ldap, removeLdapUserFromGroup, user, removeLdapUserFromGroup.getGroup(),
                    ldapGroupHolder);
        } catch (Exception ex) {
            throw new EdsIdentityException("Remove Ldap user from group error: {}", ex.getMessage());
        }
    }

    private void postRefreshEdsData(EdsLdapConfigModel.Ldap ldap, HasEdsInstanceId hasEdsInstanceId, User user,
                                    String groupName,
                                    EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group> ldapGroupHolder) {
        // 导入person资产（重写索引）
        LdapPerson.Person person = ldapPersonRepo.findPerson(ldap, EdsIdentityConverter.toLdapPerson(user));
        EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapPerson.Person> ldapPersonHolder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapPerson.Person>) holderBuilder.newHolder(
                hasEdsInstanceId.getInstanceId(), EdsAssetTypeEnum.LDAP_PERSON.name());
        ldapPersonHolder.getProvider()
                .importAsset(ldapGroupHolder.getInstance(), person);
        // 导入group资产（重写索引）
        ldapGroupHolder.getProvider()
                .importAsset(ldapGroupHolder.getInstance(), ldapGroupRepo.findGroup(ldap, groupName));
    }

    @Override
    public Set<String> queryLdapGroups(EdsIdentityParam.QueryLdapGroups queryLdapGroups) {
        getAndVerifyEdsInstance(queryLdapGroups);
        try {
            EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group> ldapGroupHolder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, LdapGroup.Group>) holderBuilder.newHolder(
                    queryLdapGroups.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
            final EdsLdapConfigModel.Ldap ldap = ldapGroupHolder.getInstance()
                    .getEdsConfigModel();
            List<LdapGroup.Group> groups = ldapGroupRepo.queryGroup(ldap);
            return groups.stream()
                    .map(LdapGroup.Group::getGroupName)
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            throw new EdsIdentityException("Query Ldap groups error: {}", ex.getMessage());
        }
    }

    private EdsInstance getAndVerifyEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        if (!IdentityUtil.hasIdentity(hasEdsInstanceId.getInstanceId())) {
            EdsIdentityException.runtime("LDAP instanceId is incorrect.");
        }
        EdsInstance instance = edsInstanceService.getById(hasEdsInstanceId.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsIdentityException.runtime("LDAP instance does not exist.");
        }
        if (!EdsInstanceTypeEnum.LDAP.name()
                .equals(instance.getEdsType())) {
            EdsIdentityException.runtime("The instance type is not LDAP.");
        }
        return instance;
    }

    private String verifyAndGeneratePassword(String password) {
        if (PasswordGenerator.isPasswordStrong(password)) {
            return password;
        }
        return PasswordGenerator.generatePassword();
    }

}
