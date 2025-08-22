package com.baiyi.cratos.eds.gitlab.event.impl;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.gitlab.GitLabEventParam;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.gitlab.event.BaseGitLabEventConsumer;
import com.baiyi.cratos.eds.gitlab.event.enums.GitLabEventName;
import com.baiyi.cratos.eds.gitlab.repo.GitLabSshKeyRepo;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.SshKey;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum.GITLAB_SSHKEY;

/**
 * @Author baiyi
 * @Date 2021/10/29 11:03 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabKeyEventConsumer extends BaseGitLabEventConsumer<SshKey> {

    private final static GitLabEventName[] EVENT_NAME_ENUMS = {GitLabEventName.KEY_CREATE, GitLabEventName.KEY_DESTROY};

    public GitLabKeyEventConsumer(EdsInstanceProviderHolderBuilder holderBuilder,
                                  EdsAssetIndexService edsAssetIndexService, EdsAssetService edsAssetService) {
        super(holderBuilder, edsAssetIndexService, edsAssetService);
    }

    @Override
    protected GitLabEventName[] getEventNameList() {
        return EVENT_NAME_ENUMS;
    }

    @Override
    protected void postProcess(EdsInstance instance, EdsAsset asset, GitLabEventParam.SystemHook systemHook) {
        // 用户删除Key
        if (!IdentityUtils.hasIdentity(systemHook.getId())) {
            return;
        }
        Long keyId = systemHook.getId();
        if (GitLabEventName.KEY_DESTROY.equals(GitLabEventName.valueOf(systemHook.getEventName()))) {
            List<EdsAsset> keyAssets = edsAssetService.queryInstanceAssetsById(instance.getId(), getAssetType(),
                    String.valueOf(keyId));
            if (!CollectionUtils.isEmpty(keyAssets)) {
                keyAssets.forEach(keyAsset -> edsAssetService.deleteById(keyAsset.getId()));
            }
        } else {
            // 用户创建新Key
            try {
                EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, SshKey> holder = getHolder(instance);
                SshKey sshKey = GitLabSshKeyRepo.getSshKey(holder.getInstance()
                        .getEdsConfigModel(), keyId);
                if (Objects.nonNull(sshKey)) {
                    holder.importAsset(sshKey);
                }
            } catch (GitLabApiException gitLabApiException) {
                log.debug(gitLabApiException.getMessage());
            }
        }
    }

    @Override
    protected String getAssetType() {
        return GITLAB_SSHKEY.name();
    }

}
