package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AwsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/5 15:06
 * &#064;Version 1.0
 */
public class AwsIamPolicyPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry param;
    private EdsIdentityVO.CloudAccount cloudAccount;

    public static AwsIamPolicyPermissionTicketEntryBuilder newBuilder() {
        return new AwsIamPolicyPermissionTicketEntryBuilder();
    }

    public AwsIamPolicyPermissionTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public AwsIamPolicyPermissionTicketEntryBuilder withCloudAccount(EdsIdentityVO.CloudAccount cloudAccount) {
        this.cloudAccount = cloudAccount;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        AwsModel.AwsPolicy awsPolicy = param.getDetail();
        int instanceId = awsPolicy.getAsset()
                .getInstanceId();
        awsPolicy.setCloudAccount(cloudAccount);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(awsPolicy.getAsset()
                        .getName())
                .displayName(awsPolicy.getAsset()
                        .getName())
                .instanceId(instanceId)
                .businessType(param.getBusinessType())
                .businessId(awsPolicy.getAsset()
                        .getId())
                .subType(EdsAssetTypeEnum.AWS_IAM_POLICY.name())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:iamUsername:{}:policyName:{}", instanceId,
                        cloudAccount.getAccountLogin()
                                .getLoginUsername(), awsPolicy.getAsset()
                                .getAssetKey()))
                .valid(true)
                .content(YamlUtils.dump(awsPolicy))
                .build();
    }

}
