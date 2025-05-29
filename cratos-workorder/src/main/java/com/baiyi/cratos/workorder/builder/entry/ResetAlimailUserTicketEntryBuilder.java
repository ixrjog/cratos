package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/28 10:13
 * &#064;Version 1.0
 */
public class ResetAlimailUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddResetAlimailUserTicketEntry param;
    private EdsIdentityVO.MailAccount mailAccount;

    public static ResetAlimailUserTicketEntryBuilder newBuilder() {
        return new ResetAlimailUserTicketEntryBuilder();
    }

    public ResetAlimailUserTicketEntryBuilder withParam(WorkOrderTicketParam.AddResetAlimailUserTicketEntry param) {
        this.param = param;
        return this;
    }

    public ResetAlimailUserTicketEntryBuilder withMailAccount(EdsIdentityVO.MailAccount mailAccount) {
        this.mailAccount = mailAccount;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(mailAccount.getAccountLogin()
                        .getLoginUsername())
                .displayName(mailAccount.getAccountLogin()
                        .getLoginUsername())
                .instanceId(mailAccount.getAccount()
                        .getInstanceId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIMAIL_USER.name())
                .businessId(mailAccount.getAccount()
                        .getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:userId:{}:mail:{}", mailAccount.getAccount()
                        .getInstanceId(), mailAccount.getAccount()
                        .getAssetId(), mailAccount.getAccountLogin()
                        .getLoginUsername()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
