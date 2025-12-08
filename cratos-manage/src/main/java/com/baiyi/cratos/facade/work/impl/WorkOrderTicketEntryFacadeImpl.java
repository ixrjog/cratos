package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.GitLabPermissionModel;
import com.baiyi.cratos.domain.model.LdapUserGroupModel;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import com.baiyi.cratos.service.*;
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
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_INSTANCE_ID;

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
    private final BusinessTagFacade businessTagFacade;
    private final BusinessTagService businessTagService;
    private final EdsAssetIndexService edsAssetIndexService;
    private final EdsInstanceService edsInstanceService;

    @Override
    public void addApplicationPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_PERMISSION, addTicketEntry.getBusinessType());
        if (Objects.nonNull(ticketEntryProvider)) {
            ticketEntryProvider.addEntry(addTicketEntry);
        }
    }

    @Override
    public void addApplicationProdPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_PROD_PERMISSION, addTicketEntry.getBusinessType());
        if (Objects.nonNull(ticketEntryProvider)) {
            ticketEntryProvider.addEntry(addTicketEntry);
        }
    }

    @Override
    public void addApplicationTestPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_TEST_PERMISSION, addTicketEntry.getBusinessType());
        if (Objects.nonNull(ticketEntryProvider)) {
            ticketEntryProvider.addEntry(addTicketEntry);
        }
    }

    @Override
    public void addApplicationElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_ELASTIC_SCALING, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addApplicationDeploymentElasticScalingTicketEntry(
            WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_ELASTIC_SCALING, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public  void addApplicationDeploymentJvmSpecTicketEntry(WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry addTicketEntry){
        TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_DEPLOYMENT_JVM_SPEC, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addComputerPermissionTicketEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddComputerPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddComputerPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.COMPUTER_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addRevokeUserPermissionTicketEntry(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.REVOKE_USER_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addLdapRolePermissionTicketEntry(WorkOrderTicketParam.AddLdapRolePermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddLdapRolePermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddLdapRolePermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.LDAP_ROLE_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addGitLabProjectPermissionTicketEntry(
            WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddGitLabProjectPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.GITLAB_PROJECT_PERMISSION, addTicketEntry.getBusinessType());
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
                WorkOrderKeys.GITLAB_GROUP_PERMISSION, addTicketEntry.getBusinessType());
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
                WorkOrderKeys.ALIYUN_DATAWORKS_AK, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAliyunRamUserTicketEntry(
            WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_RAM_USER_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAwsIamUserTicketEntry(WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.AWS_IAM_USER_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addResetAliyunRamUserTicketEntry(WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_RAM_USER_RESET, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addResetUserPasswordTicketEntry(WorkOrderTicketParam.AddResetUserPasswordTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddResetUserPasswordTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddResetUserPasswordTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.USER_RESET_PASSWORD, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addResetAwsIamUserTicketEntry(WorkOrderTicketParam.AddResetAwsIamUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddResetAwsIamUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddResetAwsIamUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.AWS_IAM_USER_RESET, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAwsTransferSftpUserTicketEntry(
            WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.AWS_TRANSFER_SFTP_USER_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addAwsIamPolicyPermissionTicketEntry(
            WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.AWS_IAM_POLICY_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addAliyunRamPolicyPermissionTicketEntry(
            WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_RAM_POLICY_PERMISSION, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateFrontEndApplicationTicketEntry(
            WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_FRONTEND_CREATE, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAliyunKmsSecretTicketEntry(
            WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_KMS_SECRET_CREATE, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addUpdateAliyunKmsSecretTicketEntry(
            WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_KMS_SECRET_UPDATE, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addApplicationDeletePodTicketEntry(
            WorkOrderTicketParam.AddApplicationDeletePodTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeletePodTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationDeletePodTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_DELETE_POD, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addApplicationRedeployTicketEntry(
            WorkOrderTicketParam.AddApplicationRedeployTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationRedeployTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationRedeployTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_REDEPLOY, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addDeploymentRedeployTicketEntry(WorkOrderTicketParam.AddDeploymentRedeployTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentRedeployTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentRedeployTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_REDEPLOY, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addDeploymentPodTicketEntry(WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.APPLICATION_DELETE_POD, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addResetAlimailUserTicketEntry(WorkOrderTicketParam.AddResetAlimailUserTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddResetAlimailUserTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddResetAlimailUserTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIMAIL_USER_RESET, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAliyunOnsTopicTicketEntry(
            WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_ONS_TOPIC, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addCreateAliyunOnsConsumerGroupTicketEntry(
            WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.ALIYUN_ONS_CONSUMER_GROUP, addTicketEntry.getBusinessType());
        Optional.ofNullable(ticketEntryProvider)
                .ifPresent(provider -> provider.addEntry(addTicketEntry));
    }

    @Override
    public void addRiskChangeTicketEntry(WorkOrderTicketParam.AddRiskChangeTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddRiskChangeTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddRiskChangeTicketEntry>) TicketEntryProviderFactory.getProvider(
                WorkOrderKeys.RISK_CHANGE, addTicketEntry.getBusinessType());
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
    public List<EdsAssetVO.Asset> queryAliyunKmsKeyTicketEntry(
            WorkOrderTicketParam.QueryAliyunKmsKeyTicketEntry queryAliyunKmsKeyTicketEntry) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(ALIYUN_KMS_INSTANCE_ID,
                queryAliyunKmsKeyTicketEntry.getKmsInstanceId());
        if (CollectionUtils.isEmpty(indices)) {
            return List.of();
        }
        return indices.stream()
                .map(index -> edsAssetService.getById(index.getAssetId()))
                .filter(Objects::nonNull)
                .map(asset -> BeanCopierUtils.copyProperties(asset, EdsAssetVO.Asset.class))
                .toList();
    }

    @Override
    public List<EdsInstanceVO.EdsInstance> queryAliyunKmsTicketEntry() {
        Tag kmsTag = tagService.getByTagKey(SysTagKeys.KMS);
        if (Objects.isNull(kmsTag)) {
            return List.of();
        }
        return queryInstanceByTag(kmsTag);
    }

    private List<EdsInstanceVO.EdsInstance> queryInstanceByTag(Tag instanceTag) {
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(instanceTag.getId())
                .businessType(BusinessTypeEnum.EDS_INSTANCE.name())
                .build();
        EdsInstanceParam.InstancePageQuery pageQuery = EdsInstanceParam.InstancePageQuery.builder()
                .page(1)
                .length(10)
                .queryByTag(queryByTag)
                .build();
        DataTable<EdsInstanceVO.EdsInstance> dataTable = edsFacade.queryEdsInstancePage(pageQuery);
        return dataTable.getData();
    }

    @Override
    public List<EdsInstanceVO.EdsInstance> queryDataWorksInstanceTicketEntry() {
        Tag dataWorksTag = tagService.getByTagKey(SysTagKeys.DATAWORKS);
        if (Objects.isNull(dataWorksTag)) {
            return List.of();
        }
        return queryInstanceByTag(dataWorksTag);
    }

    @Override
    public List<EdsInstanceVO.EdsInstance> queryRocketMqInstanceTicketEntry() {
        Tag rocketMqTag = tagService.getByTagKey(SysTagKeys.ROCKET_MQ);
        if (Objects.isNull(rocketMqTag)) {
            return List.of();
        }
        return queryInstanceByTag(rocketMqTag);
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
                    EdsAssetVO.Asset vo = BeanCopierUtils.copyProperties(asset, EdsAssetVO.Asset.class);
                    vo.setEdsInstance(BeanCopierUtils.copyProperties(edsInstanceService.getById(vo.getInstanceId()),
                            EdsInstanceVO.EdsInstance.class));
                    return vo;
                })
                .toList();
    }

    @Override
    public OptionsVO.Options getLdapGroupOptions() {
        Tag groupTag = tagService.getByTagKey(SysTagKeys.USER_GROUP);
        if (Objects.isNull(groupTag)) {
            return OptionsVO.NO_OPTIONS_AVAILABLE;
        }

        List<Integer> ldapGroupAssetIds = edsAssetService.queryAssetIdsByAssetType(EdsAssetTypeEnum.LDAP_GROUP.name());
        if (CollectionUtils.isEmpty(ldapGroupAssetIds)) {
            return OptionsVO.NO_OPTIONS_AVAILABLE;
        }
        BusinessTagParam.QueryBusinessTagValues queryBusinessTagValues = BusinessTagParam.QueryBusinessTagValues.builder()
                .tagId(groupTag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessIds(ldapGroupAssetIds)
                .build();
        List<String> values = businessTagService.queryBusinessTagValues(queryBusinessTagValues);
        return OptionsVO.toOptions(values);
    }

    @Override
    public List<LdapUserGroupModel.Role> queryLdapRolePermissionTicketEntry(
            WorkOrderTicketParam.QueryLdapRolePermissionTicketEntry queryLdapRolePermissionTicketEntry) {
        Tag groupTag = tagService.getByTagKey(SysTagKeys.USER_GROUP);
        if (groupTag == null) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(groupTag.getId())
                .tagValue(queryLdapRolePermissionTicketEntry.getGroup())
                .build();
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .assetType(EdsAssetTypeEnum.LDAP_GROUP.name())
                .queryByTag(queryByTag)
                .page(1)
                .length(100)
                .build();
        ((WorkOrderTicketEntryFacadeImpl) AopContext.currentProxy()).invokeQueryParam(pageQuery);
        DataTable<EdsAsset> dataTable = edsAssetService.queryEdsInstanceAssetPage(pageQuery.toParam());
        if (dataTable == null || CollectionUtils.isEmpty(dataTable.getData())) {
            return List.of();
        }
        return dataTable.getData()
                .stream()
                .map(e -> {
                    SimpleBusiness hasBusiness = SimpleBusiness.builder()
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .businessId(e.getId())
                            .build();
                    BusinessTag descriptionTag = businessTagFacade.getBusinessTag(hasBusiness,
                            SysTagKeys.DESCRIPTION.getKey());
                    return LdapUserGroupModel.Role.builder()
                            .asset(e)
                            .group(e.getName())
                            .description(descriptionTag != null ? descriptionTag.getTagValue() : Global.NONE)
                            .build();
                })
                .toList();
    }

    @PageQueryByTag(typeOf = BusinessTypeEnum.EDS_ASSET)
    public void invokeQueryParam(EdsInstanceParam.AssetPageQuery pageQuery) {
    }

}
