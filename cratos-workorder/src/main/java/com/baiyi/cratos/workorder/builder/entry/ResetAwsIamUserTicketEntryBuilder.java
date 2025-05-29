package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/29 13:35
 * &#064;Version 1.0
 */
public class ResetAwsIamUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddResetAwsIamUserTicketEntry param;
    private EdsIdentityVO.CloudAccount cloudAccount;


    public static ResetAwsIamUserTicketEntryBuilder newBuilder() {
        return new ResetAwsIamUserTicketEntryBuilder();
    }

    public ResetAwsIamUserTicketEntryBuilder withParam(WorkOrderTicketParam.AddResetAwsIamUserTicketEntry param) {
        this.param = param;
        return this;
    }

    public ResetAwsIamUserTicketEntryBuilder withCloudAccount(EdsIdentityVO.CloudAccount cloudAccount) {
        this.cloudAccount = cloudAccount;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(cloudAccount.getAccountLogin()
                        .getLoginUsername())
                .displayName(cloudAccount.getAccountLogin()
                        .getLoginUsername())
                .instanceId(cloudAccount.getAccount()
                        .getInstanceId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.AWS_IAM_USER.name())
                .businessId(cloudAccount.getAccount()
                        .getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:loginUsername:{}", cloudAccount.getAccount()
                        .getInstanceId(), cloudAccount.getAccountLogin()
                        .getLoginUsername()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}