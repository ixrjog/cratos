package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/20 14:29
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunRamPolicyPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry param;
    private String username;
    private EdsAliyunConfigModel.Aliyun aliyun;

    public static AliyunRamPolicyPermissionTicketEntryBuilder newBuilder() {
        return new AliyunRamPolicyPermissionTicketEntryBuilder();
    }

    public AliyunRamPolicyPermissionTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public AliyunRamPolicyPermissionTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public AliyunRamPolicyPermissionTicketEntryBuilder withAliyun(EdsAliyunConfigModel.Aliyun aliyun) {
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
        AliyunModel.AliyunPolicy aliyunPolicy = param.getDetail();
        String ramUsername = toRamUsername();
        String ramLoginUsername = toRamLoginUsername();
        aliyunPolicy.setUsername(username);
        aliyunPolicy.setRamUsername(ramUsername);
        aliyunPolicy.setRamLoginUsername(ramLoginUsername);
        aliyunPolicy.setLoginLink(aliyun.getRam()
                .toLoginUrl());
        int instanceId = aliyunPolicy.getAsset()
                .getInstanceId();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(aliyunPolicy.getAsset().getAssetKey())
                .displayName(aliyunPolicy.getAsset().getAssetKey())
                .instanceId(instanceId)
                .businessType(param.getBusinessType())
                .businessId(aliyunPolicy.getAsset()
                        .getId())
                .subType(EdsAssetTypeEnum.ALIYUN_RAM_POLICY.name())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:ramLoginUsername:{}:policyName:{}", instanceId,
                        ramLoginUsername, aliyunPolicy.getAsset()
                                .getAssetKey()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
