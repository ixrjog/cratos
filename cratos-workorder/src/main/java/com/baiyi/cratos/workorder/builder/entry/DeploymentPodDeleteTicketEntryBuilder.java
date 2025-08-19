package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/12 15:44
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeploymentPodDeleteTicketEntryBuilder {

    private WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry param;

    public static DeploymentPodDeleteTicketEntryBuilder newBuilder() {
        return new DeploymentPodDeleteTicketEntryBuilder();
    }

    public DeploymentPodDeleteTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddDeploymentPodDeleteTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        ApplicationDeploymentModel.DeleteDeploymentPod deleteDeploymentPod = param.getDetail();
        EdsAsset asset = deleteDeploymentPod.getAsset();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(deleteDeploymentPod.getPodName())
                .displayName(deleteDeploymentPod.getPodName())
                .instanceId(deleteDeploymentPod.getAsset()
                        .getInstanceId())
                .businessType(param.getBusinessType())
                .businessId(asset.getId())
                .namespace(deleteDeploymentPod.getNamespace())
                .completed(true)
                .completedAt(new Date())
                .executedAt(deleteDeploymentPod.getDeleteOperationTime())
                .success(deleteDeploymentPod.getSuccess())
                .result(deleteDeploymentPod.getResult())
                .subType(EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .entryKey(deleteDeploymentPod.getPodName()+ ":" + IdentityUtil.randomUUID())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
