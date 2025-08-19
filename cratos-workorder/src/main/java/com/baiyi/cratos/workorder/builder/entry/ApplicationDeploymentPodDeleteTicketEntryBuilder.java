package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 17:10
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationDeploymentPodDeleteTicketEntryBuilder {

    private WorkOrderTicketParam.AddApplicationDeletePodTicketEntry param;

    public static ApplicationDeploymentPodDeleteTicketEntryBuilder newBuilder() {
        return new ApplicationDeploymentPodDeleteTicketEntryBuilder();
    }

    public ApplicationDeploymentPodDeleteTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddApplicationDeletePodTicketEntry param) {
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
