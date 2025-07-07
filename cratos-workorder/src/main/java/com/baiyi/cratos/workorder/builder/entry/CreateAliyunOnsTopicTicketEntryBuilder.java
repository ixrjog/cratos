package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

/**
 * @Author 修远
 * @Date 2025/6/12 15:14
 * @Since 1.0
 */
public class CreateAliyunOnsTopicTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry param;

    public static CreateAliyunOnsTopicTicketEntryBuilder newBuilder() {
        return new CreateAliyunOnsTopicTicketEntryBuilder();
    }

    public CreateAliyunOnsTopicTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();

        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(param.getDetail().getTopicName())
                .displayName(param.getDetail().getTopicName())
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.ALIYUN_ONS_V5_TOPIC.name())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:onsInstanceId:{}:topicName:{}", instance.getId(),
                        param.getDetail().getOnsInstanceId(), param.getDetail().getTopicName()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }
}
