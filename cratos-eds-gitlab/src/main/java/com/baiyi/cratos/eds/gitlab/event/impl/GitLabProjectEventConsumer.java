package com.baiyi.cratos.eds.gitlab.event.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.gitlab.GitLabEventParam;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.gitlab.event.BaseGitLabEventConsumer;
import com.baiyi.cratos.eds.gitlab.event.enums.GitLabEventName;
import com.baiyi.cratos.eds.gitlab.repo.GitLabProjectRepo;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.GITLAB_PROJECT_ID;
import static com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum.GITLAB_PROJECT;

/**
 * @Author baiyi
 * @Date 2021/11/1 2:26 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabProjectEventConsumer extends BaseGitLabEventConsumer<Project> {

    private final static GitLabEventName[] EVENT_NAME_ENUMS = {GitLabEventName.PROJECT_CREATE, GitLabEventName.PROJECT_DESTROY, GitLabEventName.PROJECT_RENAME};

    public GitLabProjectEventConsumer(EdsInstanceProviderHolderBuilder holderBuilder,
                                      EdsAssetIndexService edsAssetIndexService, EdsAssetService edsAssetService) {
        super(holderBuilder, edsAssetIndexService, edsAssetService);
    }

    @Override
    protected GitLabEventName[] getEventNameList() {
        return EVENT_NAME_ENUMS;
    }

    @Override
    protected String getAssetType() {
        return GITLAB_PROJECT.name();
    }

    @Override
    protected void postProcess(EdsInstance instance, EdsAsset asset, GitLabEventParam.SystemHook systemHook) {
        EdsAssetIndex projectIdIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(), GITLAB_PROJECT_ID);
        if (Objects.isNull(projectIdIndex)) {
            return;
        }
        EdsAsset uk = EdsAsset.builder()
                .instanceId(instance.getId())
                .assetType(GITLAB_PROJECT.name())
                .assetKey(projectIdIndex.getValue())
                .build();
        EdsAsset projectAsset = edsAssetService.getByUniqueKey(uk);
        // 删除资产
        if (Objects.nonNull(projectAsset)) {
            edsAssetService.deleteById(projectAsset.getId());
        }
        try {
            EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, Project> holder = getHolder(instance);
            Project project = GitLabProjectRepo.getProject(holder.getInstance()
                    .getEdsConfigModel(), Long.valueOf(projectIdIndex.getValue()));
            if (Objects.nonNull(project)) {
                holder.importAsset(project);
            }
        } catch (GitLabApiException gitLabApiException) {
            log.debug(gitLabApiException.getMessage());
        }
    }

}
