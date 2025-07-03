package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.RiskChangeModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.RiskChangeTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/3 09:52
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.USER)
@WorkOrderKey(key = WorkOrderKeys.RISK_CHANGE)
public class RiskChangeTicketEntryProvider extends BaseTicketEntryProvider<RiskChangeModel.RiskChangeApplication, WorkOrderTicketParam.AddRiskChangeTicketEntry> {

    public RiskChangeTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                         WorkOrderTicketService workOrderTicketService,
                                         WorkOrderService workOrderService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
    }

    private static final String ROW_TPL = "| {} | {} |";

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Applicant | Title |
                | --- | --- |
                """;
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                RiskChangeModel.RiskChangeApplication riskChangeApplication) throws WorkOrderTicketException {
        // 不需要处理
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddRiskChangeTicketEntry addRiskChangeTicketEntry) {
        return RiskChangeTicketEntryBuilder.newBuilder()
                .withParam(addRiskChangeTicketEntry)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        RiskChangeModel.RiskChangeApplication riskChangeApplication = loadAs(entry);
        String applicantName = StringFormatter.arrayFormat("<{}|{}>", StringUtils.hasText(
                riskChangeApplication.getApplicant()
                        .getDisplayName()) ? riskChangeApplication.getApplicant()
                .getDisplayName() : riskChangeApplication.getApplicant()
                .getName(), riskChangeApplication.getApplicant()
                .getEmail());
        return StringFormatter.arrayFormat(ROW_TPL, applicantName, riskChangeApplication.getTitle());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Risk Change")
                .build();
    }

}
