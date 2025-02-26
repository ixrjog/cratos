package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.facade.EdsIdentityFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
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

    private static final List<String> CLOUD_IDENTITY_TYPES = List.of(EdsAssetTypeEnum.ALIYUN_RAM_USER.name(),
            EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER.name(), EdsAssetTypeEnum.AWS_IAM_USER.name());

    @Override
    public EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsInstanceParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
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
            EdsInstanceParam.QueryLdapIdentityDetails queryLdapIdentityDetails) {
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
            EdsInstanceParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
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
            EdsInstanceParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        User user = userService.getByUsername(queryGitLabIdentityDetails.getUsername());
        if (Objects.isNull(user)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsAssetVO.Asset> gitLabIdentities = Maps.newHashMap();
        Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Maps.newHashMap();
        Map<Integer, List<EdsAssetVO.Asset>> sshKeyMap = Maps.newHashMap();
        edsAssetService.queryByTypeAndKey(EdsAssetTypeEnum.GITLAB_USER.name(), user.getUsername())
                .forEach(asset -> {
                    gitLabIdentities.put(asset.getId(), edsAssetWrapper.wrapToTarget(asset));
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

}
