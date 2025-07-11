package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

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
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();
        AliyunModel.AliyunAccount aliyunAccount = param.getDetail();
        String account = toRamLoginUsername();
        String ramUsername = toRamUsername();
        String ramLoginUsername = toRamLoginUsername();
        aliyunAccount.setAccount(account);
        aliyunAccount.setUsername(username);
        aliyunAccount.setRamUsername(ramUsername);
        aliyunAccount.setRamLoginUsername(ramLoginUsername);
        aliyunAccount.setLoginLink(aliyun.getRam()
                .toLoginUrl());
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(ramLoginUsername)
                .displayName(ramLoginUsername)
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIYUN_RAM_USER.name())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:ramLoginUsername:{}", instance.getId(),
                        ramLoginUsername))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
