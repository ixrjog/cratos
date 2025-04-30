package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/29 14:02
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationElasticScalingTicketEntryBuilder {

    private WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry param;

    public static ApplicationElasticScalingTicketEntryBuilder newBuilder() {
        return new ApplicationElasticScalingTicketEntryBuilder();
    }

    public ApplicationElasticScalingTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddApplicationElasticScalingTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        ApplicationVO.Application application = param.getDetail()
                .getApplication();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(application.getName())
                .displayName(application.getName())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(application.getBusinessId())
                .namespace(param.getDetail().getNamespace())
                .completed(false)
                .entryKey(application.getName())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
