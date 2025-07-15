package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/15 11:23
 * &#064;Version 1.0
 */
public class ApplicationDeploymentRedeployTicketEntryBuilder {

    private WorkOrderTicketParam.AddApplicationRedeployTicketEntry param;

    public static ApplicationDeploymentRedeployTicketEntryBuilder newBuilder() {
        return new ApplicationDeploymentRedeployTicketEntryBuilder();
    }

    public ApplicationDeploymentRedeployTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddApplicationRedeployTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        ApplicationVO.Application application = param.getDetail();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(application.getName())
                .displayName(application.getName())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(application.getId())
                .completed(false)
                .entryKey(application.getApplicationName())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
