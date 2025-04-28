package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/28 14:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GitLabGroupPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry param;
    private EdsAssetIndex groupWebUrl;

    public static GitLabGroupPermissionTicketEntryBuilder newBuilder() {
        return new GitLabGroupPermissionTicketEntryBuilder();
    }

    public GitLabGroupPermissionTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public GitLabGroupPermissionTicketEntryBuilder withGroupWebUrl(EdsAssetIndex groupWebUrl) {
        this.groupWebUrl = groupWebUrl;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        String webUrl = groupWebUrl.getValue();
        EdsAssetVO.Asset groupAsset = param.getDetail().getTarget();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(groupAsset.getName())
                .displayName(webUrl)
                .instanceId(groupAsset.getInstanceId())
                .businessType(groupAsset.getBusinessType())
                .businessId(groupAsset
                        .getBusinessId())
                .completed(false)
                .entryKey(webUrl)
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
