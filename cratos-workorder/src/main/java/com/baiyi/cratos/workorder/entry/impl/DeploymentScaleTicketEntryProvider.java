package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationDeploymentScaleTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/30 11:36
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_DEPLOYMENT_SCALE)
public class DeploymentScaleTicketEntryProvider extends BaseTicketEntryProvider<ApplicationDeploymentModel.DeploymentScale, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry> {

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;
    private final EdsInstanceService edsInstanceService;

    public DeploymentScaleTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                              WorkOrderTicketService workOrderTicketService,
                                              WorkOrderService workOrderService,
                                              EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                              KubernetesDeploymentRepo kubernetesDeploymentRepo,
                                              EdsInstanceService edsInstanceService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
        this.edsInstanceService = edsInstanceService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Instance Name | Namespace | Deployment | Current Replicas | Expected Replicas |
                | --- | --- | --- | --- | --- |
                """;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationDeploymentModel.DeploymentScale deploymentScale = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), entry.getNamespace(), entry.getName(),
                deploymentScale.getCurrentReplicas(), deploymentScale.getExpectedReplicas());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Application deployment")
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationDeploymentModel.DeploymentScale applicationConfigurationChange) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsKubernetesConfigModel.Kubernetes kubernetes = holder.getInstance()
                .getEdsConfigModel();
        ApplicationDeploymentModel.DeploymentScale deploymentScale = loadAs(entry);
        // name 是否需要优化 ？
        Deployment deployment = kubernetesDeploymentRepo.scale(kubernetes, deploymentScale.getNamespace(),
                entry.getName(), deploymentScale.getExpectedReplicas());
        // 导入
        holder.importAsset(deployment);
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry param) {
        return ApplicationDeploymentScaleTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
