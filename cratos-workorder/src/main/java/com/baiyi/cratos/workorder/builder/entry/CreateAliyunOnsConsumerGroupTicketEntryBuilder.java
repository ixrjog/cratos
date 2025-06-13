package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2025/6/12 15:15
 * @Since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAliyunOnsConsumerGroupTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry param;

    public static CreateAliyunOnsConsumerGroupTicketEntryBuilder newBuilder() {
        return new CreateAliyunOnsConsumerGroupTicketEntryBuilder();
    }

    public CreateAliyunOnsConsumerGroupTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();

        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(param.getDetail().getConsumerGroupId())
                .displayName(param.getDetail().getConsumerGroupId())
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIYUN_ONS_V5_CONSUMER_GROUP.name())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:onsInstanceId:{}:consumerGroupId:{}",
                        instance.getId(),
                        param.getDetail().getOnsInstanceId(), param.getDetail().getConsumerGroupId()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
