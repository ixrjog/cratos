package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/19 11:12
 * &#064;Version 1.0
 */
public class CreateAliyunRamUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry param;
    private String username;
    private EdsAliyunConfigModel.Aliyun aliyun;

    public static CreateAliyunRamUserTicketEntryBuilder newBuilder() {
        return new CreateAliyunRamUserTicketEntryBuilder();
    }

    public CreateAliyunRamUserTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry param) {
        this.param = param;
        return this;
    }

    public CreateAliyunRamUserTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public CreateAliyunRamUserTicketEntryBuilder withAliyun(EdsAliyunConfigModel.Aliyun aliyun) {
        this.aliyun = aliyun;
        return this;
    }

    private String toAliyunAccount() {
        return username.replace(".", "")
                .toLowerCase();
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();
        AliyunModel.AliyunAccount aliyunAccount = param.getDetail();
        String account = toAliyunAccount();
        aliyunAccount.setAccount(account);
        aliyunAccount.setUsername(username);
        aliyunAccount.setLoginLink(aliyun.getRam().toLoginUrl());
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(account)
                .displayName(account)
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:username:{}", instance.getId(), username))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }
}
