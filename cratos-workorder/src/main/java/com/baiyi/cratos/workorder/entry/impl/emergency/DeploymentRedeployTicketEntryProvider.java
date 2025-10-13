package com.baiyi.cratos.workorder.entry.impl.emergency;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.exception.DaoServiceException;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.DeploymentRedeployTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/15 11:15
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_REDEPLOY)
public class DeploymentRedeployTicketEntryProvider extends BaseTicketEntryProvider<ApplicationDeploymentModel.RedeployDeployment, WorkOrderTicketParam.AddDeploymentRedeployTicketEntry> {

    private final EdsInstanceService edsInstanceService;

    public DeploymentRedeployTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                 WorkOrderTicketService workOrderTicketService,
                                                 WorkOrderService workOrderService,
                                                 EdsInstanceService edsInstanceService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.generateMarkdownSeparator(TableHeaderConstants.DEPLOYMENT_REDEPLOY);
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationDeploymentModel.RedeployDeployment redeployDeployment = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        String instanceName = Objects.nonNull(instance) ? instance.getInstanceName() : "N/A";
        return MarkdownUtils.generateMarkdownTableRow(instance.getInstanceName(), entry.getNamespace(),
                redeployDeployment.getAsset()
                        .getName(), redeployDeployment.getRedeployOperationTime());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Kubernetes Deployment")
                .build();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddDeploymentRedeployTicketEntry param) {
        WorkOrderTicketEntry entry = paramToEntry(param);
        WorkOrderTicket ticket = workOrderTicketService.getById(entry.getTicketId());
        if (!TicketState.COMPLETED.equals(TicketState.valueOf(ticket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only when the work order is completed can the configuration be added.");
        }
        try {
            workOrderTicketEntryService.add(entry);
            return entry;
        } catch (DaoServiceException daoServiceException) {
            throw new WorkOrderTicketException("Repeat adding entries.");
        }
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationDeploymentModel.RedeployDeployment redeployDeployment) throws WorkOrderTicketException {
        // ReadOnly
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddDeploymentRedeployTicketEntry param) {
        return DeploymentRedeployTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
