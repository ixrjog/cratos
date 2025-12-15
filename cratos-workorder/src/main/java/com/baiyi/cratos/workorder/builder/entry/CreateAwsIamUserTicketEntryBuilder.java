package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AwsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/3 17:24
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAwsIamUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry param;
    private String username;
    private EdsAwsConfigModel.Aws aws;

    public static CreateAwsIamUserTicketEntryBuilder newBuilder() {
        return new CreateAwsIamUserTicketEntryBuilder();
    }

    public CreateAwsIamUserTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry  param) {
        this.param = param;
        return this;
    }

    public CreateAwsIamUserTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public CreateAwsIamUserTicketEntryBuilder withAws(EdsAwsConfigModel.Aws aws) {
        this.aws = aws;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();
        AwsModel.AwsAccount awsAccount = param.getDetail();
        awsAccount.setUsername(this.username);
        awsAccount.setAccountId(this.aws.getCred().getId());
        awsAccount.setIamUsername(this.username);
        awsAccount.setLoginUsername(this.username);
        awsAccount.setLoginLink(aws.getIam().toLoginUrl());
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(this.username)
                .displayName(this.username)
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIYUN_RAM_USER.name())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat(
                        "instanceId:{}:iamUsername:{}", instance.getId(),
                        this.username
                ))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
