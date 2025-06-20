package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyun.sdk.service.kms20160120.models.CreateSecretResponseBody;
import com.aliyun.sdk.service.kms20160120.models.DescribeSecretResponseBody;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunKmsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunKms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunKmsRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.AliyunKmsSecretCreateTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/20 10:45
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_KMS_SECRET_CREATE)
public class AliyunKmsSecretCreateTicketEntryProvider extends BaseTicketEntryProvider<AliyunKmsModel.CreateSecret, WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry> {

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} | {} |";
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    public AliyunKmsSecretCreateTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                    WorkOrderTicketService workOrderTicketService,
                                                    WorkOrderService workOrderService,
                                                    EdsInstanceService edsInstanceService,
                                                    EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        AliyunKmsModel.CreateSecret createSecret = param.getDetail();
        // 我要偷懒了

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunKmsModel.CreateSecret createSecret) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        Optional<CreateSecretResponseBody> optionalCreateSecretResponseBody = AliyunKmsRepo.createSecret(
                createSecret.getEndpoint(), aliyun, createSecret.getSecretName(), createSecret.getVersionId(),
                createSecret.getEncryptionKeyId(), createSecret.getSecretData(), createSecret.getDescription());
        if (optionalCreateSecretResponseBody.isEmpty()) {
            WorkOrderTicketException.runtime("Failed to create KMS secret: " + createSecret.getSecretName());
        }
        Optional<DescribeSecretResponseBody> optionalDescribeSecretResponseBody = AliyunKmsRepo.describeSecret(
                createSecret.getEndpoint(), aliyun, createSecret.getSecretName());
        if (optionalDescribeSecretResponseBody.isEmpty()) {
            WorkOrderTicketException.runtime("Failed to describe KMS secret: " + createSecret.getSecretName());
        }
        // 写入资产
        try {
            AliyunKms.SecretMetadata metadata = BeanCopierUtil.copyProperties(optionalDescribeSecretResponseBody.get(),
                    AliyunKms.SecretMetadata.class);
            AliyunKms.Secret secret = AliyunKms.Secret.builder()
                    .secretName(createSecret.getSecretName())
                    .secretType(metadata.getSecretType())
                    .createTime(metadata.getCreateTime())
                    .updateTime(metadata.getUpdateTime())
                    .build();
            AliyunKms.KmsSecret kmsSecret = AliyunKms.KmsSecret.builder()
                    .endpoint(createSecret.getEndpoint())
                    .metadata(metadata)
                    .secret(secret)
                    .build();
            holder.importAsset(kmsSecret);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry addCreateAliyunKmsSecretTicketEntry) {
        int instanceId = Optional.ofNullable(addCreateAliyunKmsSecretTicketEntry)
                .map(WorkOrderTicketParam.TicketEntry::getInstanceId)
                .orElseThrow(() -> new WorkOrderTicketException("Instance ID is required"));
        if (!IdentityUtil.hasIdentity(instanceId)) {
            WorkOrderTicketException.runtime("Invalid instance ID: " + instanceId);
        }
        EdsInstance instance = edsInstanceService.getById(instanceId);
        if (instance == null) {
            WorkOrderTicketException.runtime("Instance not found: " + instanceId);
        }
        return AliyunKmsSecretCreateTicketEntryBuilder.newBuilder()
                .withParam(addCreateAliyunKmsSecretTicketEntry)
                .withEdsInstance(BeanCopierUtil.copyProperties(instance, EdsInstanceVO.EdsInstance.class))
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aliyun Instance | Secret Name | Version ID | Encryption Key Id | Config Center Value | Description |
                | --- | --- | --- | --- | --- | --- |
                """;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunKmsModel.CreateSecret createSecret = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), createSecret.getSecretName(),
                createSecret.getVersionId(), createSecret.getEncryptionKeyId(), "KMS#" + createSecret.getSecretName(),
                createSecret.getDescription());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("KMS Secret")
                .build();
    }

}
