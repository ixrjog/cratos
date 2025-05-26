package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/21 16:50
 * &#064;Version 1.0
 */
public class ResetAliyunRamUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry param;
    private String username;
    private EdsAliyunConfigModel.Aliyun aliyun;

    public static ResetAliyunRamUserTicketEntryBuilder newBuilder() {
        return new ResetAliyunRamUserTicketEntryBuilder();
    }

    public ResetAliyunRamUserTicketEntryBuilder withParam(WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry param) {
        this.param = param;
        return this;
    }

    public ResetAliyunRamUserTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ResetAliyunRamUserTicketEntryBuilder withAliyun(EdsAliyunConfigModel.Aliyun aliyun) {
        this.aliyun = aliyun;
        return this;
    }

    private String toRamLoginUsername() {
        return this.aliyun.getRam()
                .toUsername(username.replace(".", "")
                        .toLowerCase());
    }

    private String toRamUsername() {
        return username.replace(".", "")
                .toLowerCase();
    }

    public WorkOrderTicketEntry buildEntry() {
        AliyunModel.ResetAliyunAccount resetAliyunAccount = param.getDetail();
        String account = toRamLoginUsername();
        String ramUsername = toRamUsername();
        String ramLoginUsername = toRamLoginUsername();
        resetAliyunAccount.setAccount(account);
        resetAliyunAccount.setUsername(username);
        resetAliyunAccount.setRamUsername(ramUsername);
        resetAliyunAccount.setRamLoginUsername(ramLoginUsername);
        resetAliyunAccount.setLoginLink(aliyun.getRam()
                .toLoginUrl());
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(ramLoginUsername)
                .displayName(ramLoginUsername)
                .instanceId(resetAliyunAccount.getAsset().getInstanceId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIYUN_RAM_USER.name())
                .businessId(resetAliyunAccount.getAsset().getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:ramLoginUsername:{}", resetAliyunAccount.getAsset().getInstanceId(),
                        ramLoginUsername))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
