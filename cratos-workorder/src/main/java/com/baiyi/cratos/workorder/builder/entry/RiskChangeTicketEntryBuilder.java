package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.RiskChangeModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/3 10:23
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RiskChangeTicketEntryBuilder {

    private WorkOrderTicketParam.AddRiskChangeTicketEntry param;

    public static RiskChangeTicketEntryBuilder newBuilder() {
        return new RiskChangeTicketEntryBuilder();
    }

    public RiskChangeTicketEntryBuilder withParam(WorkOrderTicketParam.AddRiskChangeTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        RiskChangeModel.RiskChangeApplication riskChangeApplication = param.getDetail();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(riskChangeApplication.getTitle())
                .displayName(riskChangeApplication.getTitle())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(riskChangeApplication.getApplicant()
                        .getBusinessId())
                .completed(false)
                .entryKey(IdentityUtils.randomUUID())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}