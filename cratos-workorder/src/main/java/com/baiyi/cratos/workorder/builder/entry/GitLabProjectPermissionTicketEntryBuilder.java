package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 18:12
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GitLabProjectPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry param;

    private EdsAssetIndex projectGitSshUrl;

    public static GitLabProjectPermissionTicketEntryBuilder newBuilder() {
        return new GitLabProjectPermissionTicketEntryBuilder();
    }

    public GitLabProjectPermissionTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public GitLabProjectPermissionTicketEntryBuilder withProjectGitSshUrl(EdsAssetIndex projectGitSshUrl) {
        this.projectGitSshUrl = projectGitSshUrl;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        String gitSshUrl = projectGitSshUrl.getValue();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(gitSshUrl)
                .displayName(gitSshUrl)
                .instanceId(param.getDetail().getTarget().getInstanceId())
                .businessType(param.getBusinessType())
                .businessId(param.getDetail().getTarget()
                        .getBusinessId())
                .completed(false)
                .entryKey(gitSshUrl)
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
