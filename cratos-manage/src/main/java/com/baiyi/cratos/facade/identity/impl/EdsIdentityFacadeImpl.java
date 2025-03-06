package com.baiyi.cratos.facade.identity.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.identity.EdsIdentityFacade;
import com.baiyi.cratos.facade.identity.extension.EdsCloudIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.EdsLdapIdentityExtension;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import com.google.api.client.util.Lists;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    private final EdsLdapIdentityExtension ldapIdentityExtension;
    private final EdsCloudIdentityExtension edsCloudIdentityExtension;

    private static final List<String> CLOUD_IDENTITY_TYPES = List.of(EdsAssetTypeEnum.ALIYUN_RAM_USER.name(),
            EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER.name(), EdsAssetTypeEnum.AWS_IAM_USER.name());

    @Override
    public EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
        return edsCloudIdentityExtension.queryCloudIdentityDetails(queryCloudIdentityDetails);
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

    @Override
    public EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity) {
        return ldapIdentityExtension.createLdapIdentity(createLdapIdentity);
    }

    @Override
    public EdsIdentityVO.LdapIdentity resetLdapUserPassword(
            EdsIdentityParam.ResetLdapUserPassword resetLdapUserPassword) {
        return ldapIdentityExtension.resetLdapUserPassword(resetLdapUserPassword);
    }

    @Override
    public void deleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity) {
        ldapIdentityExtension.deleteLdapIdentity(deleteLdapIdentity);
    }

    @Override
    public void addLdapUserToGroup(EdsIdentityParam.AddLdapUserToGroup addLdapUserToGroup) {
        ldapIdentityExtension.addLdapUserToGroup(addLdapUserToGroup);
    }

    @Override
    public void removeLdapUserFromGroup(EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup) {
        ldapIdentityExtension.removeLdapUserFromGroup(removeLdapUserFromGroup);
    }

    @Override
    public Set<String> queryLdapGroups(EdsIdentityParam.QueryLdapGroups queryLdapGroups) {
        return ldapIdentityExtension.queryLdapGroups(queryLdapGroups);
    }

    private List<EdsAssetVO.Asset> querySshKeys(String username, int instanceId) {
        return edsAssetService.queryByTypeAndName(EdsAssetTypeEnum.GITLAB_SSHKEY.name(), username, false)
                .stream()
                .filter(e -> e.getInstanceId() == instanceId)
                .map(edsAssetWrapper::wrapToTarget)
                .toList();
    }

    private Map<Integer, EdsAsset> getAssetMapByMobile(String mobilePhone) {
        if (!StringUtils.hasText(mobilePhone)) {
            return Map.of();
        }
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(DINGTALK_USER_MOBILE, mobilePhone);
        if (CollectionUtils.isEmpty(indices)) {
            return Map.of();
        }
        return indices.stream()
                .collect(Collectors.toMap(EdsAssetIndex::getAssetId, e -> edsAssetService.getById(e.getAssetId())));
    }

    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        return edsCloudIdentityExtension.createCloudAccount(createCloudAccount);
    }

    @Override
    public void grantCloudAccountPermission(EdsIdentityParam.GrantPermission grantPermission) {
        edsCloudIdentityExtension.grantCloudAccountPermission(grantPermission);
    }

    @Override
    public void revokeCloudAccountPermission(EdsIdentityParam.RevokePermission revokePermission) {
        edsCloudIdentityExtension.revokeCloudAccountPermission(revokePermission);
    }

}
