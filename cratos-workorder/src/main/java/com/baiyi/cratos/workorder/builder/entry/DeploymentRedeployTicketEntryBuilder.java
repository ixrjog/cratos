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
 * &#064;Date  2025/7/15 11:28
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeploymentRedeployTicketEntryBuilder {

    private WorkOrderTicketParam.AddDeploymentRedeployTicketEntry param;

    public static DeploymentRedeployTicketEntryBuilder newBuilder() {
        return new DeploymentRedeployTicketEntryBuilder();
    }

    public DeploymentRedeployTicketEntryBuilder withParam(WorkOrderTicketParam.AddDeploymentRedeployTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        ApplicationDeploymentModel.RedeployDeployment redeployDeployment = param.getDetail();
        EdsAsset asset = redeployDeployment.getAsset();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(redeployDeployment.getAsset()
                        .getName())
                .displayName(redeployDeployment.getAsset()
                        .getName())
                .instanceId(redeployDeployment.getAsset()
                        .getInstanceId())
                .businessType(param.getBusinessType())
                .businessId(asset.getId())
                .namespace(redeployDeployment.getNamespace())
                .completed(true)
                .completedAt(new Date())
                .executedAt(redeployDeployment.getRedeployOperationTime())
                .success(redeployDeployment.getSuccess())
                .result(redeployDeployment.getResult())
                .subType(EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name())
                .entryKey(redeployDeployment.getAsset()
                        .getName() + ":" + IdentityUtil.randomUUID())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
