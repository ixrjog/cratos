package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.GitLabPermissionModel;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:33
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class WorkOrderTicketEntryFacadeImpl implements WorkOrderTicketEntryFacade {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final TicketWorkflowFacade workflowFacade;
    private final TagService tagService;
    private final EdsFacade edsFacade;
    private final ApplicationResourceService applicationResourceService;
    private final EdsAssetService edsAssetService;

    @Override
    public void addApplicationPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_PERMISSION.name(), addTicketEntry.getBusinessType());
        if (Objects.nonNull(ticketEntryProvider)) {
            ticketEntryProvider.addEntry(addTicketEntry);
        }
    }

    @Override
    public void addApplicationElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_ELASTIC_SCALING.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addApplicationDeploymentElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_ELASTIC_SCALING.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addComputerPermissionTicketEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddComputerPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddComputerPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.COMPUTER_PERMISSION.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addRevokeUserPermissionTicketEntry(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.REVOKE_USER_PERMISSION.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addGitLabProjectPermissionTicketEntry(
            WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.GITLAB_PROJECT_PERMISSION.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(addTicketEntry.getDetail())
                .map(GitLabPermissionModel.Permission::getRole)
                .orElseThrow(() -> new WorkOrderTicketException("GitLab project permission role cannot be empty."));
        Optional.of(addTicketEntry.getDetail())
                .map(GitLabPermissionModel.Permission::getTarget)
                .orElseThrow(() -> new WorkOrderTicketException("GitLab project permission target cannot be empty."));
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addGitLabGroupPermissionTicketEntry(
            WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddGitLabGroupPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.GITLAB_GROUP_PERMISSION.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(addTicketEntry.getDetail())
                .map(GitLabPermissionModel.Permission::getRole)
                .orElseThrow(() -> new WorkOrderTicketException("GitLab group permission role cannot be empty."));
        Optional.of(addTicketEntry.getDetail())
                .map(GitLabPermissionModel.Permission::getTarget)
                .orElseThrow(() -> new WorkOrderTicketException("GitLab group permission target cannot be empty."));
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addAliyunDataWorksInstanceTicketEntry(
            WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_DATAWORKS_AK.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAliyunRamUserTicketEntry(
            WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_RAM_USER_PERMISSION.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addAliyunRamPolicyPermissionTicketEntry(
            WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_RAM_POLICY_PERMISSION.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addApplicationDeletePodTicketEntry(
            WorkOrderTicketParam.AddApplicationDeletePodTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeletePodTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeletePodTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_DELETE_POD.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addDeploymentPodTicketEntry(WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_DELETE_POD.name(), addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void setValidById(int id) {
        WorkOrderTicketEntry workOrderTicketEntry = workOrderTicketEntryService.getById(id);
        if (Objects.isNull(workOrderTicketEntry)) {
            return;
        }
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(workOrderTicketEntry.getTicketId());
        if (!TicketState.IN_APPROVAL.equals(TicketState.valueOf(workOrderTicket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only the in approval status can be set as valid.");
        }
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        WorkOrderTicketNode workOrderTicketNode = workOrderTicketNodeService.getById(workOrderTicket.getNodeId());
        if (!workflowFacade.isApprover(workOrderTicket, workOrderTicketNode.getNodeName(),
                SessionUtils.getUsername())) {
            WorkOrderTicketException.runtime("Only the approver of the current node can set valid settings.");
        }
        workOrderTicketEntryService.updateValidById(id);
    }

    @Override
    public void deleteTicketEntry(WorkOrderTicketParam.DeleteTicketEntry deleteTicketEntry) {
        WorkOrderTicketEntry workOrderTicketEntry = workOrderTicketEntryService.getByUniqueKey(
                deleteTicketEntry.totEntryUniqueKey());
        if (Objects.nonNull(workOrderTicketEntry)) {
            deleteById(workOrderTicketEntry.getId());
        }
    }

    @Override
    public void deleteByTicketId(int ticketId) {
        List<WorkOrderTicketEntry> workOrderTicketEntries = workOrderTicketEntryService.queryTicketEntries(ticketId);
        if (!CollectionUtils.isEmpty(workOrderTicketEntries)) {
            for (WorkOrderTicketEntry workOrderTicketEntry : workOrderTicketEntries) {
                workOrderTicketEntryService.deleteById(workOrderTicketEntry.getId());
            }
        }
    }

    @Override
    public void deleteById(int id) {
        WorkOrderTicketEntry workOrderTicketEntry = workOrderTicketEntryService.getById(id);
        if (Objects.isNull(workOrderTicketEntry)) {
            return;
        }
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(workOrderTicketEntry.getTicketId());
        if (!workOrderTicket.getUsername()
                .equals(SessionUtils.getUsername())) {
            WorkOrderTicketException.runtime("Only the applicant can delete the entry.");
        }
        if (!TicketState.NEW.equals(TicketState.valueOf(workOrderTicket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only in the new state can it be deleted.");
        }
        workOrderTicketEntryService.deleteById(id);
    }

    @Override
    public void deleteAllByTicketId(int ticketId) {
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(ticketId);
        if (!workOrderTicket.getUsername()
                .equals(SessionUtils.getUsername())) {
            WorkOrderTicketException.runtime("Only the applicant can delete the entry.");
        }
        if (!TicketState.NEW.equals(TicketState.valueOf(workOrderTicket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only in the new state can it be deleted.");
        }
        deleteByTicketId(ticketId);
    }

    @Override
    public List<EdsInstanceVO.EdsInstance> queryDataWorksInstanceTicketEntry() {
        Tag dataWorksTag = tagService.getByTagKey(SysTagKeys.DATAWORKS);
        if (Objects.isNull(dataWorksTag)) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(dataWorksTag.getId())
                .businessType(BusinessTypeEnum.EDS_INSTANCE.name())
                .build();
        EdsInstanceParam.InstancePageQuery pageQuery = EdsInstanceParam.InstancePageQuery.builder()
                .page(1)
                .length(5)
                .queryByTag(queryByTag)
                .build();
        DataTable<EdsInstanceVO.EdsInstance> dataTable = edsFacade.queryEdsInstancePage(pageQuery);
        return dataTable.getData();
    }

    @Override
    public List<EdsAssetVO.Asset> queryApplicationResourceDeploymentTicketEntry(
            WorkOrderTicketParam.QueryApplicationResourceDeploymentTicketEntry queryApplicationResourceDeploymentTicketEntry) {
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                queryApplicationResourceDeploymentTicketEntry.getApplicationName(),
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(),
                queryApplicationResourceDeploymentTicketEntry.getNamespace());
        if (CollectionUtils.isEmpty(resources)) {
            return List.of();
        }
        return resources.stream()
                .map(e -> {
                    EdsAsset asset = edsAssetService.getById(e.getBusinessId());
                    return BeanCopierUtil.copyProperties(asset, EdsAssetVO.Asset.class);
                })
                .toList();
    }

}
