package com.baiyi.cratos.workorder.entry.impl.emergency;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/12 09:53
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_DELETE_POD)
public class DeploymentPodDeleteTicketEntryProvider extends BaseTicketEntryProvider<ApplicationDeploymentModel.DeleteDeploymentPod, WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry> {

    private final EdsInstanceService edsInstanceService;

    public DeploymentPodDeleteTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                  WorkOrderTicketService workOrderTicketService,
                                                  WorkOrderService workOrderService,
                                                  EdsInstanceService edsInstanceService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Instance Name | Namespace | Deployment Name | Pod Name | Delete Operation Time |
                | --- | --- | --- | --- | --- |
                """;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationDeploymentModel.DeleteDeploymentPod deleteDeploymentPod = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        String instanceName = Objects.nonNull(instance) ? instance.getInstanceName() : "N/A";
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), entry.getNamespace(), deleteDeploymentPod.getPodName(), deleteDeploymentPod.getDeleteOperationTime());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Kubernetes Pod")
                .build();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationDeploymentModel.DeleteDeploymentPod deleteDeploymentPod) throws WorkOrderTicketException {
        // ReadOnly
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry param) {
        // TODO
        return null;
    }

}
