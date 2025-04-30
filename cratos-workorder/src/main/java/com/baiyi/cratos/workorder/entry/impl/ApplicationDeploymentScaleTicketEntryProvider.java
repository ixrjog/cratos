package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationDeploymentScaleTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/30 11:36
 * &#064;Version 1.0
 */
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_DEPLOYMENT_SCALE)
public class ApplicationDeploymentScaleTicketEntryProvider extends BaseTicketEntryProvider<ApplicationDeploymentModel.DeploymentScale, WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry> {

    public ApplicationDeploymentScaleTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                         WorkOrderTicketService workOrderTicketService,
                                                         WorkOrderService workOrderService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Deployment Name | Namespace | Current Replicas | Expected Replicas |
                | --- | --- | --- | --- |
                """;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} |";

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationDeploymentModel.DeploymentScale deploymentScale = loadAs(entry);
        return StringFormatter.arrayFormat(ROW_TPL, entry.getName(), entry.getNamespace(),
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

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationDeploymentModel.DeploymentScale applicationConfigurationChange) throws WorkOrderTicketException {
        // TODO: Implement the logic to process the entry
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry param) {
        return ApplicationDeploymentScaleTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
