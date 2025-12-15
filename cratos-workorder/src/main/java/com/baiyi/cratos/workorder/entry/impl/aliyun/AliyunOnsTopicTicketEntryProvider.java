package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyun.rocketmq20220801.models.GetTopicResponseBody;
import com.aliyun.rocketmq20220801.models.ListTopicsResponseBody;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunOnsV5Model;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunOnsV5;
import com.baiyi.cratos.eds.aliyun.repo.AliyunOnsV5Repo;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.CreateAliyunOnsTopicTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author 修远
 * @Date 2025/6/12 11:28
 * @Since 1.0
 */

@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_ONS_TOPIC)
public class AliyunOnsTopicTicketEntryProvider extends BaseTicketEntryProvider<AliyunOnsV5Model.Topic, WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    public AliyunOnsTopicTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                             WorkOrderTicketService workOrderTicketService,
                                             WorkOrderService workOrderService, EdsInstanceService edsInstanceService,
                                             EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.ALIYUN_ONS_TOPIC);
    }

    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        String topicName = param.getDetail()
                .getTopicName();
        if (StringUtils.isBlank(topicName)) {
            WorkOrderTicketException.runtime("Topic name can not be blank！");
        }
        if (!topicName.startsWith("TOPIC_")) {
            WorkOrderTicketException.runtime("Topic name must be start with TOPIC_ ！");
        }
        if (!ValidationUtils.isAliyunOnsTopic(topicName)) {
            WorkOrderTicketException.runtime("Invalid topic name！");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunOnsV5Model.Topic topic) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, ListTopicsResponseBody.ListTopicsResponseBodyDataList> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, ListTopicsResponseBody.ListTopicsResponseBodyDataList>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_ONS_V5_TOPIC.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        createTopic(aliyun, topic);
        // 导入资产
        GetTopicResponseBody.GetTopicResponseBodyData newTopic = getTopic(aliyun, topic);
        ListTopicsResponseBody.ListTopicsResponseBodyDataList data = new ListTopicsResponseBody.ListTopicsResponseBodyDataList().setInstanceId(
                        topic.getOnsInstanceId())
                .setTopicName(newTopic.getTopicName())
                .setMessageType(newTopic.getMessageType())
                .setRemark(newTopic.getRemark())
                .setRegionId(newTopic.getRegionId())
                .setStatus(newTopic.getStatus())
                .setUpdateTime(newTopic.getUpdateTime())
                .setCreateTime(newTopic.getCreateTime());
        EdsAsset asset = holder.importAsset(data);
    }

    private GetTopicResponseBody.GetTopicResponseBodyData getTopic(EdsAliyunConfigModel.Aliyun aliyun,
                                                                   AliyunOnsV5Model.Topic topic) {
        try {
            return AliyunOnsV5Repo.getTopic(topic.getRegionId(), aliyun, topic.getOnsInstanceId(),
                    topic.getTopicName());
        } catch (Exception e) {
            throw new WorkOrderTicketException("Failed to get Aliyun ONS Topic err: {}", e.getMessage());
        }
    }

    private void createTopic(EdsAliyunConfigModel.Aliyun aliyun, AliyunOnsV5Model.Topic topic) {
        try {
            AliyunOnsV5.CreateTopic createTopic = AliyunOnsV5.CreateTopic.builder()
                    .topicName(topic.getTopicName())
                    .messageType(topic.getMessageType())
                    .remark(topic.getRemark())
                    .build();
            AliyunOnsV5Repo.createTopic(topic.getRegionId(), aliyun, topic.getOnsInstanceId(), createTopic);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Failed to create Aliyun ONS topic err: {}", e.getMessage());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry param) {
        Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateAliyunOnsTopicTicketEntry::getDetail)
                .map(AliyunOnsV5Model.Topic::getEdsInstance)
                .map(EdsInstanceVO.EdsInstance::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Eds instanceId is null"));
        return CreateAliyunOnsTopicTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunOnsV5Model.Topic topic = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(instance.getInstanceName(), topic.getOnsInstanceName(),
                topic.getTopicName(), topic.getMessageType(), topic.getRemark());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Aliyun ONS Topic")
                .build();
    }

}