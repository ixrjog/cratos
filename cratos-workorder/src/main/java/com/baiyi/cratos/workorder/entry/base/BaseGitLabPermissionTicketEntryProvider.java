package com.baiyi.cratos.workorder.entry.base;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.model.GitLabPermissionModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.config.EdsGitLabConfigModel;
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
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/28 14:44
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
public abstract class BaseGitLabPermissionTicketEntryProvider<EntryParam extends WorkOrderTicketParam.TicketEntry<GitLabPermissionModel.Permission>> extends BaseTicketEntryProvider<GitLabPermissionModel.Permission, EntryParam> {

    protected final EdsInstanceService edsInstanceService;
    protected final EdsAssetIndexService edsAssetIndexService;
    protected final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final GitLabUserFacade gitLabUserFacade;
    protected final GitLabProjectFacade gitLabProjectFacade;
    protected final GitLabGroupFacade gitLabGroupFacade;
    protected final EdsAssetService edsAssetService;

    public BaseGitLabPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService,
                                                   EdsInstanceService edsInstanceService,
                                                   EdsAssetIndexService edsAssetIndexService,
                                                   EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                   GitLabUserFacade gitLabUserFacade,
                                                   GitLabProjectFacade gitLabProjectFacade,
                                                   GitLabGroupFacade gitLabGroupFacade,
                                                   EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsAssetIndexService = edsAssetIndexService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.gitLabUserFacade = gitLabUserFacade;
        this.gitLabProjectFacade = gitLabProjectFacade;
        this.gitLabGroupFacade = gitLabGroupFacade;
        this.edsAssetService = edsAssetService;
    }

    protected org.gitlab4j.api.models.User getOrCreateUser(EdsGitLabConfigModel.GitLab gitLab,
                                                           String username) throws WorkOrderTicketException {
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

    protected User createGitLabUser(EdsGitLabConfigModel.GitLab gitLab,
                                    String username) throws WorkOrderTicketException {
        try {
            return gitLabUserFacade.createUser(gitLab, username);
        } catch (GitLabApiException e) {
            throw new WorkOrderTicketException("Failed to create GitLab user {}: {}", username, e.getMessage());
        }
    }

}
