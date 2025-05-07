package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/30 15:02
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationDeploymentScaleTicketEntryBuilder {

    private WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry param;

    public static ApplicationDeploymentScaleTicketEntryBuilder newBuilder() {
        return new ApplicationDeploymentScaleTicketEntryBuilder();
    }

    public ApplicationDeploymentScaleTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddApplicationDeploymentScaleTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsAssetVO.Asset deployment = param.getDetail()
                .getDeployment();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(deployment.getName())
                .displayName(deployment.getName())
                .instanceId(deployment.getInstanceId())
                .businessType(param.getBusinessType())
                .businessId(deployment.getId())
                .namespace(param.getDetail().getNamespace())
                .completed(false)
                .entryKey(Joiner.on(":").join(param.getDetail().getNamespace(), deployment.getName()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
