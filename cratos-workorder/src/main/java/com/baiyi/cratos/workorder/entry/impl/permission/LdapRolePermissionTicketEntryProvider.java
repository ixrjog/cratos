package com.baiyi.cratos.workorder.entry.impl.permission;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.GitLabPermissionModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.gitlab.facade.GitLabGroupFacade;
import com.baiyi.cratos.eds.gitlab.facade.GitLabProjectFacade;
import com.baiyi.cratos.eds.gitlab.facade.GitLabUserFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.GitLabGroupPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseGitLabPermissionTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.GitLabAccessLevelConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.REPO_WEB_URL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/26 14:38
 * &#064;Version 1.0
 */
@Slf4j
//@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.LDAP_ROLE_PERMISSION)
public class LdapRolePermissionTicketEntryProvider extends BaseGitLabPermissionTicketEntryProvider<WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry> {

    private static final String TABLE_TITLE = """
            | Platform | Type | Role | Desc | Docs Link |
            | --- | --- | --- | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} | {} | {} |";

    public LdapRolePermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                 WorkOrderTicketService workOrderTicketService,
                                                 WorkOrderService workOrderService,
                                                 EdsInstanceService edsInstanceService,
                                                 EdsAssetIndexService edsAssetIndexService,
                                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                 GitLabUserFacade gitLabUserFacade,
                                                 GitLabProjectFacade gitLabProjectFacade,
                                                 GitLabGroupFacade gitLabGroupFacade, EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService, edsInstanceService,
                edsAssetIndexService, edsInstanceProviderHolderBuilder, gitLabUserFacade, gitLabProjectFacade,
                gitLabGroupFacade, edsAssetService);
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return TABLE_TITLE;
    }

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        GitLabPermissionModel.Permission groupPermission = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        String webUrl = Optional.ofNullable(edsAssetIndexService.getByAssetIdAndName(groupPermission.getTarget()
                        .getId(), REPO_WEB_URL))
                .map(EdsAssetIndex::getValue)
                .orElse("-");
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), entry.getName(), webUrl,
                groupPermission.getRole());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                GitLabPermissionModel.Permission projectPermission) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, ?> holder = (EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, ?>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.GITLAB_GROUP.name());
        EdsGitLabConfigModel.GitLab gitLab = holder.getInstance()
                .getEdsConfigModel();
        String role = projectPermission.getRole();
        org.gitlab4j.api.models.User gitLabUser = getOrCreateUser(gitLab, workOrderTicket.getUsername());
        Long gitLabUserId = gitLabUser.getId();
        GitLabAccessLevelConstants gitlabAccessLevel = Arrays.stream(GitLabAccessLevelConstants.values())
                .filter(e -> e.getRole()
                        .equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(
                        () -> new WorkOrderTicketException("The GitLab role name applied for is incorrect: role={}",
                                role));
        AccessLevel accessLevel = AccessLevel.forValue(gitlabAccessLevel.getAccessValue());
        EdsAsset gitLabGroupAsset = edsAssetService.getById(entry.getBusinessId());
        if (Objects.isNull(gitLabGroupAsset)) {
            WorkOrderTicketException.runtime("GitLab group asset not found: id={}", entry.getBusinessId());
        }
        final Long gitLabGroupId = Long.valueOf(gitLabGroupAsset.getAssetId());
        try {
            List<Member> groupMembers = gitLabGroupFacade.getMembers(gitLab, gitLabGroupId);
            Optional<Member> optionalGroupMember = groupMembers.stream()
                    .filter(e -> e.getId()
                            .equals(gitLabUser.getId()))
                    .findFirst();
            if (optionalGroupMember.isPresent()) {
                if (accessLevel.equals(optionalGroupMember.get()
                        .getAccessLevel())) {
                    // 角色一致
                    return;
                }
                log.debug("Work order update user gitLab group member: userId={}, groupId={}, accessLevel={}",
                        gitLabUserId, gitLabGroupId, accessLevel.name());
                gitLabGroupFacade.updateMember(gitLab, gitLabGroupId, gitLabUserId, accessLevel);
            } else {
                log.info("Work order add user gitLab group member: userId={}, groupId={}, accessLevel={}", gitLabUserId,
                        gitLabGroupId, accessLevel.name());
                gitLabGroupFacade.addMember(gitLab, gitLabGroupId, gitLabUserId, accessLevel);
            }
        } catch (GitLabApiException gitLabApiException) {
            throw new WorkOrderTicketException("GitLab API err: {}", gitLabApiException.getMessage());
        }
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry param) {
        EdsAssetIndex webUrlIndex = edsAssetIndexService.getByAssetIdAndName(param.getDetail()
                .getTarget()
                .getId(), REPO_WEB_URL);
        if (Objects.isNull(webUrlIndex)) {
            throw new WorkOrderTicketException("GitLab group assets are missing web url.");
        }
        return GitLabGroupPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withGroupWebUrl(webUrlIndex)
                .buildEntry();
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("LDAP role permission")
                .build();
    }

}
