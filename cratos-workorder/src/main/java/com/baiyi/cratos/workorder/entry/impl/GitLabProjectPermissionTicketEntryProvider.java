package com.baiyi.cratos.workorder.entry.impl;


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
import com.baiyi.cratos.eds.gitlab.facade.GitLabProjectFacade;
import com.baiyi.cratos.eds.gitlab.facade.GitLabUserFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.GitLabProjectPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.GitLabAccessLevelConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.REPO_SSH_URL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 17:46
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.GITLAB_PROJECT_PERMISSION)
public class GitLabProjectPermissionTicketEntryProvider extends BaseTicketEntryProvider<GitLabPermissionModel.Permission, WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final EdsAssetIndexService edsAssetIndexService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final GitLabUserFacade gitLabUserFacade;
    private final GitLabProjectFacade gitLabProjectFacade;
    private final EdsAssetService edsAssetService;

    public GitLabProjectPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                      WorkOrderTicketService workOrderTicketService,
                                                      WorkOrderService workOrderService,
                                                      EdsInstanceService edsInstanceService,
                                                      EdsAssetIndexService edsAssetIndexService,
                                                      EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                      GitLabUserFacade gitLabUserFacade,
                                                      GitLabProjectFacade gitLabProjectFacade,
                                                      EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsAssetIndexService = edsAssetIndexService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.gitLabUserFacade = gitLabUserFacade;
        this.gitLabProjectFacade = gitLabProjectFacade;
        this.edsAssetService = edsAssetService;
    }

    private static final String TABLE_TITLE = """
            | Instance Name | Project SshURL | Role |
            | --- | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} | {} |";

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
        GitLabPermissionModel.Permission projectPermission = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), entry.getName(),
                projectPermission.getRole());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                GitLabPermissionModel.Permission projectPermission) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, ?> holder = (EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, ?>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.GITLAB_PROJECT.name());

        EdsGitLabConfigModel.GitLab gitLab = holder.getInstance()
                .getEdsConfigModel();

        String username = workOrderTicket.getUsername();
        String role = projectPermission.getRole();
        org.gitlab4j.api.models.User gitLabUser = getOrCreateUser(gitLab, username);
        Long gitLabUserId = gitLabUser.getId();
        GitLabAccessLevelConstants gitlabAccessLevel = Arrays.stream(GitLabAccessLevelConstants.values())
                .filter(e -> e.getRole()
                        .equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(
                        () -> new WorkOrderTicketException("The Gitlab role name applied for is incorrect: role={}",
                                role));
        org.gitlab4j.api.models.AccessLevel accessLevel = org.gitlab4j.api.models.AccessLevel.forValue(
                gitlabAccessLevel.getAccessValue());

        EdsAsset gitLabProjectAsset = edsAssetService.getById(entry.getBusinessId());
        if (Objects.isNull(gitLabProjectAsset)) {
            WorkOrderTicketException.runtime("GitLab project asset not found: id={}", entry.getBusinessId());
        }
        final Long gitLabProjectId = Long.valueOf(gitLabProjectAsset.getAssetId());
        try {
            List<Member> projectMembers = gitLabProjectFacade.getProjectMembers(gitLab, gitLabProjectId);
            Optional<Member> optionalProjectMember = projectMembers.stream()
                    .filter(e -> e.getId()
                            .equals(gitLabUser.getId()))
                    .findFirst();
            if (optionalProjectMember.isPresent()) {
                if (!accessLevel.value.equals(optionalProjectMember.get()
                        .getAccessLevel().value)) {
                    log.debug("Work order update user gitLab project member: userId={}, projectId={}, accessLevel={}",
                            gitLabUserId, gitLabProjectId, accessLevel.name());
                    gitLabProjectFacade.updateProjectMember(gitLab, gitLabProjectId, gitLabUserId, accessLevel);
                }
            } else {
                log.info("Work order create user gitLab project member: userId={}, projectId={}, accessLevel={}",
                        gitLabUserId, gitLabProjectId, accessLevel.name());
                gitLabProjectFacade.addProjectMember(gitLab, gitLabProjectId, gitLabUserId, accessLevel);
            }
        } catch (GitLabApiException gitLabApiException) {
            if ( gitLabApiException.getMessage().contains("is not included in the list")) {
                throw new WorkOrderTicketException("GitLab added project member error: does not support authorizing {} roles", accessLevel.name());
            }
            throw new WorkOrderTicketException("GitLab API err: {}", gitLabApiException.getMessage());
        }
    }

    private User getOrCreateUser(EdsGitLabConfigModel.GitLab gitLab, String username) throws WorkOrderTicketException {
        try {
            List<org.gitlab4j.api.models.User> gitLabUsers = gitLabUserFacade.findUsers(gitLab, username);
            return gitLabUsers.stream()
                    .filter(user -> user.getUsername()
                            .equals(username))
                    .findFirst()
                    .orElseGet(() -> createGitLabUser(gitLab, username));
        } catch (GitLabApiException e) {
            throw new WorkOrderTicketException("Error handling GitLab user {}: {}", username, e.getMessage());
        }
    }

    private User createGitLabUser(EdsGitLabConfigModel.GitLab gitLab, String username) throws WorkOrderTicketException {
        try {
            return gitLabUserFacade.createUser(gitLab, username);
        } catch (GitLabApiException e) {
            throw new WorkOrderTicketException("Failed to create GitLab user {}: {}", username, e.getMessage());
        }
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry param) {
        EdsAssetIndex sshUrlIndex = edsAssetIndexService.getByAssetIdAndName(param.getDetail()
                .getTarget()
                .getId(), REPO_SSH_URL);
        if (Objects.isNull(sshUrlIndex)) {
            throw new WorkOrderTicketException("GitLab project assets are missing ssh url.");
        }
        return GitLabProjectPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withProjectGitSshUrl(sshUrlIndex)
                .buildEntry();
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        GitLabPermissionModel.Permission projectPermission = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(projectPermission.getRole())
                .desc("ServerAccount permission")
                .build();
    }

}
