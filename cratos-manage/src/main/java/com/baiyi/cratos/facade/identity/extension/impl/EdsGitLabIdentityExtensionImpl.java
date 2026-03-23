package com.baiyi.cratos.facade.identity.extension.impl;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsGitLabIdentityExtension;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.gitlab.repo.GitLabUserRepo;
import com.baiyi.cratos.facade.identity.extension.base.BaseEdsIdentityExtension;
import com.baiyi.cratos.facade.identity.extension.context.EdsIdentityExtensionContext;
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

    public EdsGitLabIdentityExtensionImpl(EdsIdentityExtensionContext context) {
        super(context);
    }

    @Override
    public EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        final String username = queryGitLabIdentityDetails.getUsername();
        User user = context.getUserService()
                .getByUsername(username);
        if (Objects.isNull(user)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        List<EdsAsset> assets = onlyInTheInstance(
                context.getEdsAssetService()
                        .queryByTypeAndKey(EdsAssetTypeEnum.GITLAB_USER.name(), user.getUsername()),
                queryGitLabIdentityDetails
        );
        if (CollectionUtils.isEmpty(assets)) {
            return EdsIdentityVO.GitLabIdentityDetails.NO_DATA;
        }
        List<EdsIdentityVO.GitLabIdentity> gitLabIdentities = assets.stream()
                .map(asset -> EdsIdentityVO.GitLabIdentity.builder()
                        .username(username)
                        .user(context.getUserWrapper()
                                      .wrapToTarget(user))
                        .account(context.getEdsAssetWrapper()
                                         .wrapToTarget(asset))
                        .instance(context.getEdsInstanceWrapper()
                                          .wrapToTarget(context.getEdsInstanceService()
                                                                .getById(asset.getInstanceId())))
                        .sshKeys(queryGitLabUserSshKeys(user.getUsername(), asset.getInstanceId()))
                        .accountLogin(toAccountLogin(asset, username))
                        .avatar(getAvatar(asset))
                        .build())
                .toList();
        return EdsIdentityVO.GitLabIdentityDetails.builder()
                .gitLabIdentities(gitLabIdentities)
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void blockGitLabIdentity(EdsIdentityParam.BlockGitLabIdentity blockGitLabIdentity) {
        EdsInstance instance = getEdsInstance(blockGitLabIdentity);
        try {
            EdsInstanceProviderHolder<EdsConfigs.GitLab, org.gitlab4j.api.models.User> edsInstanceProviderHolder = (EdsInstanceProviderHolder<EdsConfigs.GitLab, org.gitlab4j.api.models.User>) context.getEdsProviderHolderFactory()
                    .createHolder(blockGitLabIdentity.getInstanceId(), EdsAssetTypeEnum.GITLAB_USER.name());

            org.gitlab4j.api.models.User gitLabUser = GitLabUserRepo.getUser(
                    edsInstanceProviderHolder.getInstance()
                            .getConfig(), blockGitLabIdentity.getUserId()
            );
            if (Objects.nonNull(gitLabUser)) {
                // blockUser
                GitLabUserRepo.blockUser(
                        edsInstanceProviderHolder.getInstance()
                                .getConfig(), blockGitLabIdentity.getUserId()
                );
                edsInstanceProviderHolder.importAsset(GitLabUserRepo.getUser(
                        edsInstanceProviderHolder.getInstance()
                                .getConfig(), blockGitLabIdentity.getUserId()
                ));
            }
        } catch (Exception ex) {
            throw new EdsIdentityException("Block gitLab user error: {}", ex.getMessage());
        }
    }

    private EdsIdentityVO.AccountLoginDetails toAccountLogin(EdsAsset asset, String username) {
        EdsConfigs.GitLab gitLab = (EdsConfigs.GitLab) context.getEdsProviderHolderFactory()
                .createHolder(asset.getInstanceId(), EdsAssetTypeEnum.GITLAB_USER.name())
                .getInstance()
                .getConfig();
        return EdsIdentityVO.AccountLoginDetails.builder()
                .loginUsername(username)
                .username(username)
                .loginUrl(gitLab.getApi()
                                  .getUrl())
                .build();
    }

    private List<EdsAssetVO.Asset> queryGitLabUserSshKeys(String username, int instanceId) {
        return context.getEdsAssetService()
                .queryByTypeAndName(EdsAssetTypeEnum.GITLAB_SSHKEY.name(), username, false)
                .stream()
                .filter(e -> e.getInstanceId() == instanceId)
                .map(context.getEdsAssetWrapper()::wrapToTarget)
                .toList();
    }

    private EdsInstance getEdsInstance(HasEdsInstanceId hasEdsInstanceId) {
        return getAndVerifyEdsInstance(hasEdsInstanceId, EdsInstanceTypeEnum.GITLAB);
    }

}
