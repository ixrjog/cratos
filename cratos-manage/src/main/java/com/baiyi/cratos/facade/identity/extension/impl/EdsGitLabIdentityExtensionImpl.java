package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.eds.core.facade.EdsGitLabIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/10 11:46
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class EdsGitLabIdentityExtensionImpl extends BaseEdsIdentityExtension implements EdsGitLabIdentityExtension {

    public EdsGitLabIdentityExtensionImpl(EdsAssetWrapper edsAssetWrapper, EdsInstanceService edsInstanceService,
                                          EdsInstanceWrapper edsInstanceWrapper, UserService userService,
                                          UserWrapper userWrapper, EdsInstanceProviderHolderBuilder holderBuilder,
                                          EdsAssetService edsAssetService, EdsFacade edsFacade,
                                          EdsAssetIndexService edsAssetIndexService, TagService tagService,
                                          BusinessTagService businessTagService) {
        super(edsAssetWrapper, edsInstanceService, edsInstanceWrapper, userService, userWrapper, holderBuilder,
                edsAssetService, edsFacade, edsAssetIndexService, tagService, businessTagService);
    }

    @Override
    public EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        final String username = queryGitLabIdentityDetails.getUsername();
        User user = userService.getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        List<EdsAsset> assets = onlyInTheInstance(
                edsAssetService.queryByTypeAndKey(EdsAssetTypeEnum.GITLAB_USER.name(), user.getUsername()),
                queryGitLabIdentityDetails);
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.GitLabIdentity> gitLabIdentities = assets.stream()
                .map(asset -> EdsIdentityVO.GitLabIdentity.builder()
                        .username(username)
                        .user(userWrapper.wrapToTarget(user))
                        .account(edsAssetWrapper.wrapToTarget(asset))
                        .instance(edsInstanceWrapper.wrapToTarget(edsInstanceService.getById(asset.getInstanceId())))
                        .sshKeys(queryGitLabUserSshKeys(user.getUsername(), asset.getInstanceId()))
                        .accountLogin(toAccountLogin(asset, username))
                        .avatar(getAvatar(asset))
                        .build())
                .toList();
        return EdsIdentityVO.GitLabIdentityDetails.builder()
                .gitLabIdentities(gitLabIdentities)
                .build();
    }

    private EdsIdentityVO.AccountLoginDetails toAccountLogin(EdsAsset asset, String username) {
        EdsGitLabConfigModel.GitLab gitLab = (EdsGitLabConfigModel.GitLab) holderBuilder.newHolder(
                        asset.getInstanceId(), EdsAssetTypeEnum.GITLAB_USER.name())
                .getInstance()
                .getEdsConfigModel();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .loginUsername(username)
                .username(username)
                .loginUrl(gitLab.getApi()
                        .getUrl())
                .build();
    }

    private List<EdsAssetVO.Asset> queryGitLabUserSshKeys(String username, int instanceId) {
        return edsAssetService.queryByTypeAndName(EdsAssetTypeEnum.GITLAB_SSHKEY.name(), username, false)
                .stream()
                .filter(e -> e.getInstanceId() == instanceId)
                .map(edsAssetWrapper::wrapToTarget)
                .toList();
    }

}
