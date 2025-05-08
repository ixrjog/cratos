package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/6 13:33
 * &#064;Version 1.0
 */
@Component
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_ELASTIC_SCALING)
public class ApplicationDeploymentScaleTicketEntryProvider extends DeploymentScaleTicketEntryProvider {

    public ApplicationDeploymentScaleTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                         WorkOrderTicketService workOrderTicketService,
                                                         WorkOrderService workOrderService,
                                                         EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                         KubernetesDeploymentRepo kubernetesDeploymentRepo,
                                                         EdsInstanceService edsInstanceService,
                                                         EdsAssetIndexService edsAssetIndexService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService, edsInstanceProviderHolderBuilder,
                kubernetesDeploymentRepo, edsInstanceService, edsAssetIndexService);
    }

}
