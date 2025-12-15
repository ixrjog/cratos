package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyun.rocketmq20220801.models.GetConsumerGroupResponseBody;
import com.aliyun.rocketmq20220801.models.ListConsumerGroupsResponseBody;
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
import com.baiyi.cratos.workorder.builder.entry.CreateAliyunOnsConsumerGroupTicketEntryBuilder;
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
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_ONS_CONSUMER_GROUP)
public class AliyunOnsConsumerGroupTicketEntryProvider extends BaseTicketEntryProvider<AliyunOnsV5Model.ConsumerGroup, WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    public AliyunOnsConsumerGroupTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                     WorkOrderTicketService workOrderTicketService,
                                                     WorkOrderService workOrderService,
                                                     EdsInstanceService edsInstanceService,
                                                     EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.ALIYUN_ONS_CONSUMER_GROUP);
    }

    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        String consumerGroupId = param.getDetail()
                .getConsumerGroupId();
        if (StringUtils.isBlank(consumerGroupId)) {
            WorkOrderTicketException.runtime("verifyEntryParam failed: consumer group can not be blank！");
        }
        if (!consumerGroupId.startsWith("GID_")) {
            WorkOrderTicketException.runtime("verifyEntryParam failed: consumer group must be start with GID_ ！");
        }
        if (!ValidationUtils.isAliyunOnsConsumerGroup(consumerGroupId)) {
            WorkOrderTicketException.runtime("verifyEntryParam failed: invalid consumer group！");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunOnsV5Model.ConsumerGroup consumerGroup) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_ONS_V5_CONSUMER_GROUP.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        createConsumerGroup(aliyun, consumerGroup);
        // 导入资产
        GetConsumerGroupResponseBody.GetConsumerGroupResponseBodyData newConsumerGroup = getConsumerGroup(aliyun,
                consumerGroup);
        ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList data = new ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList().setInstanceId(
                        newConsumerGroup.getInstanceId())
                .setConsumerGroupId(newConsumerGroup.getConsumerGroupId())
                .setRemark(newConsumerGroup.getRemark())
                .setRegionId(newConsumerGroup.getRegionId())
                .setStatus(newConsumerGroup.getStatus())
                .setUpdateTime(newConsumerGroup.getUpdateTime())
                .setCreateTime(newConsumerGroup.getCreateTime());
        EdsAsset asset = holder.importAsset(data);
    }

    private GetConsumerGroupResponseBody.GetConsumerGroupResponseBodyData getConsumerGroup(
            EdsAliyunConfigModel.Aliyun aliyun, AliyunOnsV5Model.ConsumerGroup consumerGroup) {
        try {
            return AliyunOnsV5Repo.getConsumerGroup(consumerGroup.getRegionId(), aliyun,
                    consumerGroup.getOnsInstanceId(), consumerGroup.getConsumerGroupId());
        } catch (Exception e) {
            throw new WorkOrderTicketException("Failed to get Aliyun ONS Topic err: {}", e.getMessage());
        }
    }

    private void createConsumerGroup(EdsAliyunConfigModel.Aliyun aliyun, AliyunOnsV5Model.ConsumerGroup consumerGroup) {
        try {

            AliyunOnsV5.ConsumeRetryPolicy consumeRetryPolicy = AliyunOnsV5.ConsumeRetryPolicy.builder()
                    .deadLetterTargetTopic(consumerGroup.getConsumeRetryPolicy()
                            .getDeadLetterTargetTopic())
                    .maxRetryTimes(consumerGroup.getConsumeRetryPolicy()
                            .getMaxRetryTimes())
                    .retryPolicy(consumerGroup.getConsumeRetryPolicy()
                            .getRetryPolicy())
                    .build();
            AliyunOnsV5.CreateConsumerGroup createConsumerGroup = AliyunOnsV5.CreateConsumerGroup.builder()
                    .consumerGroupId(consumerGroup.getConsumerGroupId())
                    .deliveryOrderType(consumerGroup.getDeliveryOrderType())
                    .remark(consumerGroup.getRemark())
                    .consumeRetryPolicy(consumeRetryPolicy)
                    .build();
            AliyunOnsV5Repo.createConsumerGroup(consumerGroup.getRegionId(), aliyun, consumerGroup.getOnsInstanceId(),
                    createConsumerGroup);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Failed to create Aliyun ONS Consumer Group err: {}", e.getMessage());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry param) {
        Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateAliyunOnsConsumerGroupTicketEntry::getDetail)
                .map(AliyunOnsV5Model.ConsumerGroup::getEdsInstance)
                .map(EdsInstanceVO.EdsInstance::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Eds instanceId is null"));
        return CreateAliyunOnsConsumerGroupTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunOnsV5Model.ConsumerGroup consumerGroup = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(instance.getInstanceName(), consumerGroup.getOnsInstanceName(),
                consumerGroup.getConsumerGroupId(), consumerGroup.getDeliveryOrderType(), consumerGroup.getRemark());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Aliyun ONS Consumer Group")
                .build();
    }

}
