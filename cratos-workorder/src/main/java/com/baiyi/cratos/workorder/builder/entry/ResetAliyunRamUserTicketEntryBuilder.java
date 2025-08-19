package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/21 16:50
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResetAliyunRamUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry param;
    private AliyunModel.ResetAliyunAccount resetAliyunAccount;

    public static ResetAliyunRamUserTicketEntryBuilder newBuilder() {
        return new ResetAliyunRamUserTicketEntryBuilder();
    }

    public ResetAliyunRamUserTicketEntryBuilder withParam(WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry param) {
        this.param = param;
        return this;
    }

    public ResetAliyunRamUserTicketEntryBuilder withResetAliyunAccount(AliyunModel.ResetAliyunAccount cloudAccount) {
        this.resetAliyunAccount = cloudAccount;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(resetAliyunAccount.getAccountLogin()
                        .getLoginUsername())
                .displayName(resetAliyunAccount.getAccountLogin()
                        .getLoginUsername())
                .instanceId(resetAliyunAccount.getAccount()
                        .getInstanceId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIYUN_RAM_USER.name())
                .businessId(resetAliyunAccount.getAccount()
                        .getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:loginUsername:{}", resetAliyunAccount.getAccount()
                        .getInstanceId(), resetAliyunAccount.getAccountLogin()
                        .getLoginUsername()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
