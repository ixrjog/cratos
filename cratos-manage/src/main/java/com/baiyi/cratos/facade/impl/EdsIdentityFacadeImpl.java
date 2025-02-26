package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.converter.EdsIdentityConverter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.repo.LdapPersonRepo;
import com.baiyi.cratos.facade.EdsIdentityFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Sets;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 10:20
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsIdentityFacadeImpl implements EdsIdentityFacade {

    private final EdsAssetWrapper edsAssetWrapper;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceWrapper edsInstanceWrapper;
    private final EdsAssetIndexService edsAssetIndexService;
    private final UserService userService;
    private final UserWrapper userWrapper;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final LdapPersonRepo ldapPersonRepo;


    private static final List<String> CLOUD_IDENTITY_TYPES = List.of(EdsAssetTypeEnum.ALIYUN_RAM_USER.name(),
            EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER.name(), EdsAssetTypeEnum.AWS_IAM_USER.name());

    @Override
    public EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
        List<EdsAsset> cloudIdentityAssets;
        Set<EdsAsset> uniqueValues = Sets.newHashSet();
        cloudIdentityAssets = CLOUD_IDENTITY_TYPES.stream()
                .map(type -> EdsAssetTypeEnum.AWS_IAM_USER.name()
                        .equals(type) ? edsAssetService.queryByTypeAndName(type,
                        queryCloudIdentityDetails.getUsername(), false) : edsAssetService.queryByTypeAndKey(type,
                        queryCloudIdentityDetails.getUsername()))
                .flatMap(Collection::stream)
                .filter(uniqueValues::add)
                .collect(Collectors.toList());
        if (cloudIdentityAssets.isEmpty()) {
            return EdsIdentityVO.CloudIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = cloudIdentityAssets.stream()
                .map(EdsAsset::getInstanceId)
                .distinct()
                .collect(Collectors.toMap(id -> id, id -> Optional.ofNullable(edsInstanceService.getById(id))
                        .map(edsInstanceWrapper::wrapToTarget)
                        .orElseThrow(
                                () -> new EdsAssetException("The edsInstance does not exist: instanceId={}.", id))));
        Map<Integer, List<String>> policyMap = Maps.newHashMap();
        cloudIdentityAssets.forEach(asset -> {
            EdsAssetIndex index = edsAssetIndexService.getByAssetIdAndName(asset.getId(), toPolicyIndexName(asset));
            if (Objects.nonNull(index)) {
                policyMap.put(asset.getId(), Lists.newArrayList(Splitter.on(",")
                        .split(index.getValue())));
            }
        });
        return EdsIdentityVO.CloudIdentityDetails.builder()
                .username(queryCloudIdentityDetails.getUsername())
                .cloudIdentities(makeCloudIdentities(cloudIdentityAssets))
                .instanceMap(instanceMap)
                .policyMap(policyMap)
                .build();
    }

    @Override
    public EdsIdentityVO.LdapIdentityDetails queryLdapIdentityDetails(
            EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails) {
        List<EdsAsset> assets = edsAssetService.queryByTypeAndKey(EdsAssetTypeEnum.LDAP_PERSON.name(),
                queryLdapIdentityDetails.getUsername());
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.LdapIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsAssetVO.Asset> ldapIdentities = Maps.newHashMap();
        Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Maps.newHashMap();
        Map<Integer, List<String>> ldapGroupMap = Maps.newHashMap();
        assets.forEach(asset -> {
            ldapIdentities.put(asset.getInstanceId(), edsAssetWrapper.wrapToTarget(asset));
            instanceMap.put(asset.getInstanceId(),
                    edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())));
            EdsAssetIndex index = edsAssetIndexService.getByAssetIdAndName(asset.getId(), LDAP_USER_GROUPS);
            if (Objects.nonNull(index)) {
                ldapGroupMap.put(asset.getId(), Lists.newArrayList(Splitter.on(";")
                        .split(index.getValue())));
            }
        });
        return EdsIdentityVO.LdapIdentityDetails.builder()
                .username(queryLdapIdentityDetails.getUsername())
                .ldapIdentities(ldapIdentities)
                .ldapGroupMap(ldapGroupMap)
                .instanceMap(instanceMap)
                .build();
    }

    @Override
    public EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        User user = userService.getByUsername(queryDingtalkIdentityDetails.getUsername());
        if (Objects.isNull(user)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsAsset> assetMap = getAssetMapByMobile(user.getMobilePhone());
        if (CollectionUtils.isEmpty(assetMap)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsAssetVO.Asset> dingtalkIdentities = Maps.newHashMap();
        Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Maps.newHashMap();
        assetMap.forEach((k, asset) -> {
            dingtalkIdentities.put(asset.getInstanceId(), edsAssetWrapper.wrapToTarget(asset));
            instanceMap.put(asset.getInstanceId(),
                    edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())));
        });
        return EdsIdentityVO.DingtalkIdentityDetails.builder()
                .dingtalkIdentities(dingtalkIdentities)
                .instanceMap(instanceMap)
                .build();
    }

    @Override
    public EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        User user = userService.getByUsername(queryGitLabIdentityDetails.getUsername());
        if (Objects.isNull(user)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsAssetVO.Asset> gitLabIdentities = Maps.newHashMap();
        Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Maps.newHashMap();
        Map<Integer, List<EdsAssetVO.Asset>> sshKeyMap = Maps.newHashMap();
        edsAssetService.queryByTypeAndKey(EdsAssetTypeEnum.GITLAB_USER.name(), user.getUsername())
                .forEach(asset -> {
                    gitLabIdentities.put(asset.getInstanceId(), edsAssetWrapper.wrapToTarget(asset));
                    instanceMap.put(asset.getInstanceId(),
                            edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())));
                    List<EdsAsset> assets = edsAssetService.queryByTypeAndName(EdsAssetTypeEnum.GITLAB_SSHKEY.name(),
                            user.getUsername(), false);
                    sshKeyMap.put(asset.getId(), querySshKeys(user.getUsername(), asset.getInstanceId()));
                });
        return EdsIdentityVO.GitLabIdentityDetails.builder()
                .gitLabIdentities(gitLabIdentities)
                .instanceMap(instanceMap)
                .sshKeyMap(sshKeyMap)
                .build();
    }

    private List<EdsAssetVO.Asset> querySshKeys(String username, int instanceId) {
        return edsAssetService.queryByTypeAndName(EdsAssetTypeEnum.GITLAB_SSHKEY.name(), username, false)
                .stream()
                .filter(e -> e.getInstanceId() == instanceId)
                .map(edsAssetWrapper::wrapToTarget)
                .toList();
    }

    private Map<String, Map<Integer, List<EdsAssetVO.Asset>>> makeCloudIdentities(List<EdsAsset> cloudIdentityAssets) {
        Map<String, Map<Integer, List<EdsAssetVO.Asset>>> cloudIdentities = Maps.newHashMap();
        cloudIdentityAssets.forEach(cloudIdentityAsset -> {
            String assetType = cloudIdentityAsset.getAssetType();
            EdsAssetVO.Asset assetVO = edsAssetWrapper.wrapToTarget(cloudIdentityAsset);
            cloudIdentities.computeIfAbsent(assetType, k -> Maps.newHashMap())
                    .computeIfAbsent(cloudIdentityAsset.getInstanceId(), k -> Lists.newArrayList())
                    .add(assetVO);
        });
        return cloudIdentities;
    }

    private String toPolicyIndexName(EdsAsset asset) {
        if (asset.getAssetType()
                .equals(EdsAssetTypeEnum.ALIYUN_RAM_USER.name())) {
            return ALIYUN_RAM_POLICIES;
        }
        if (asset.getAssetType()
                .equals(EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER.name())) {
            return HUAWEICLOUD_IAM_POLICIES;
        }
        if (asset.getAssetType()
                .equals(EdsAssetTypeEnum.AWS_IAM_USER.name())) {
            return AWS_IAM_POLICIES;
        }
        return "Unsupported types";
    }

    private Map<Integer, EdsAsset> getAssetMapByMobile(String mobilePhone) {
        Map<Integer, EdsAsset> assetMap = Maps.newHashMap();
        if (StringUtils.hasText(mobilePhone)) {
            List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(DINGTALK_USER_MOBILE,
                    mobilePhone);
            if (!CollectionUtils.isEmpty(indices)) {
                indices.forEach(e -> assetMap.put(e.getAssetId(), edsAssetService.getById(e.getAssetId())));
            }
        }
        return assetMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity) {
        EdsInstance instance = edsInstanceService.getById(createLdapIdentity.getInstanceId());
        if (Objects.isNull(instance)) {
            EdsIdentityException.runtime("LDAP instance does not exist.");
        }
        if (!EdsInstanceTypeEnum.LDAP.name()
                .equals(instance.getEdsType())) {
            EdsIdentityException.runtime("The instance type is not LDAP.");
        }
        User user = userService.getByUsername(createLdapIdentity.getUsername());
        if (Objects.isNull(user)) {
            EdsIdentityException.runtime("User {} does not exist.", createLdapIdentity.getUsername());
        }
        final String password = verifyAndGeneratePassword(createLdapIdentity.getPassword());
        // TODO 判断账户是否过期，锁定
        LdapPerson.Person person = EdsIdentityConverter.toLdapPerson(user);
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

    private String verifyAndGeneratePassword(String password) {
        if (PasswordGenerator.isPasswordStrong(password)) {
            return password;
        }
        return PasswordGenerator.generatePassword();
    }

}
