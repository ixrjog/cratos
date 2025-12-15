package com.baiyi.cratos.eds.gitlab.event.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.event.GitLabEventParam;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.gitlab.event.BaseGitLabEventConsumer;
import com.baiyi.cratos.eds.gitlab.event.enums.GitLabEventName;
import com.baiyi.cratos.eds.gitlab.repo.GitLabGroupRepo;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.GITLAB_GROUP_ID;
import static com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum.GITLAB_GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/30 09:51
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class GitLabGroupEventConsumer extends BaseGitLabEventConsumer<Group> {

    private final static GitLabEventName[] EVENT_NAME_ENUMS = {GitLabEventName.GROUP_CREATE, GitLabEventName.GROUP_DESTROY, GitLabEventName.GROUP_RENAME};

    public GitLabGroupEventConsumer(EdsInstanceProviderHolderBuilder holderBuilder,
                                    EdsAssetIndexService edsAssetIndexService, EdsAssetService edsAssetService) {
        super(holderBuilder, edsAssetIndexService, edsAssetService);
    }

    @Override
    protected GitLabEventName[] getEventNameList() {
        return EVENT_NAME_ENUMS;
    }

    @Override
    protected String getAssetType() {
        return GITLAB_GROUP.name();
    }

    @Override
    protected void postProcess(EdsInstance instance, EdsAsset asset, GitLabEventParam.SystemHook systemHook) {
        EdsAssetIndex groupIdIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(), GITLAB_GROUP_ID);
        if (Objects.isNull(groupIdIndex)) {
            return;
        }
        EdsAsset uk = EdsAsset.builder()
                .instanceId(instance.getId())
                .assetType(GITLAB_GROUP.name())
                .assetKey(groupIdIndex.getValue())
                .build();
        EdsAsset groupAsset = edsAssetService.getByUniqueKey(uk);
        // 删除资产
        if (Objects.nonNull(groupAsset)) {
            edsAssetService.deleteById(groupAsset.getId());
        }
        try {
            EdsInstanceProviderHolder<EdsConfigs.GitLab, Group> holder = getHolder(instance);
            Group group = GitLabGroupRepo.getGroup(holder.getInstance()
                    .getConfig(), Long.valueOf(groupIdIndex.getValue()));
            if (Objects.nonNull(group)) {
                holder.importAsset(group);
            }
        } catch (GitLabApiException gitLabApiException) {
            log.debug(gitLabApiException.getMessage());
        }
    }

}
