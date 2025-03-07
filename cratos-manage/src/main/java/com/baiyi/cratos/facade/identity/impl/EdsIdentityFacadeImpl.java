package com.baiyi.cratos.facade.identity.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
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
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
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

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_USER_MOBILE;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.LDAP_USER_GROUPS;

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
        String username = queryLdapIdentityDetails.getUsername();
        User user = userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.LdapIdentityDetails.NO_DATA;
        }
        List<EdsAsset> assets = edsAssetService.queryByTypeAndKey(EdsAssetTypeEnum.LDAP_PERSON.name(), username);
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.LdapIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.LdapIdentity> ldapIdentities = assets.stream()
                .map(asset -> {
                    EdsAssetIndex index = edsAssetIndexService.getByAssetIdAndName(asset.getId(), LDAP_USER_GROUPS);
                    List<String> groups = Objects.nonNull(index) ? Splitter.on(";")
                            .splitToList(index.getValue()) : List.of();
                    return EdsIdentityVO.LdapIdentity.builder()
                            .username(username)
                            .user(userWrapper.wrapToTarget(user))
                            .instance(
                                    edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                            .account(edsAssetWrapper.wrapToTarget(asset))
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
    public EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        String username = queryDingtalkIdentityDetails.getUsername();
        User user = userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        Map<Integer, EdsAsset> assetMap = getAssetMapByMobile(user.getMobilePhone());
        if (CollectionUtils.isEmpty(assetMap)) {
            return EdsIdentityVO.DingtalkIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.DingtalkIdentity> dingtalkIdentities = assetMap.values()
                .stream()
                .map(asset -> EdsIdentityVO.DingtalkIdentity.builder()
                        .username(username)
                        .user(userWrapper.wrapToTarget(user))
                        .instance(edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                        .account(edsAssetWrapper.wrapToTarget(asset))
                        .build())
                .toList();
        return EdsIdentityVO.DingtalkIdentityDetails.builder()
                .username(username)
                .dingtalkIdentities(dingtalkIdentities)
                .build();
    }

    @Override
    public EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        String username = queryGitLabIdentityDetails.getUsername();
        User user = userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.GitLabIdentity> gitLabIdentities = Lists.newArrayList();

        edsAssetService.queryByTypeAndKey(EdsAssetTypeEnum.GITLAB_USER.name(), user.getUsername())
                .forEach(asset -> {
                    EdsIdentityVO.GitLabIdentity gitLabIdentity = EdsIdentityVO.GitLabIdentity.builder()
                            .username(username)
                            .user(userWrapper.wrapToTarget(user))
                            .account( edsAssetWrapper.wrapToTarget(asset))
                            .instance(  edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                            .sshKeys( querySshKeys(user.getUsername(), asset.getInstanceId()))
                            .build();

                    gitLabIdentities.add(gitLabIdentity);
                });
        return EdsIdentityVO.GitLabIdentityDetails.builder()
                .gitLabIdentities(gitLabIdentities)
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
