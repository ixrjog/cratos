package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyun.sdk.service.kms20160120.models.CreateSecretResponseBody;
import com.aliyun.sdk.service.kms20160120.models.DescribeSecretResponseBody;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.InfoSummaryUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AliyunKmsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunKms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunKmsRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.AliyunKmsSecretCreateTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_ENDPOINT;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CONTENT_HASH;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/20 10:45
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_KMS_SECRET_CREATE)
public class AliyunKmsSecretCreateTicketEntryProvider extends BaseTicketEntryProvider<AliyunKmsModel.CreateSecret, WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry> {

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} | {} |";
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsAssetIndexService edsAssetIndexService;
    private final StringEncryptor stringEncryptor;
    private final LanguageUtils languageUtils;
    private final EdsAssetService edsAssetService;

    public AliyunKmsSecretCreateTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                    WorkOrderTicketService workOrderTicketService,
                                                    WorkOrderService workOrderService,
                                                    EdsInstanceService edsInstanceService,
                                                    EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                    EdsAssetIndexService edsAssetIndexService,
                                                    StringEncryptor stringEncryptor, LanguageUtils languageUtils,
                                                    EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsAssetIndexService = edsAssetIndexService;
        this.stringEncryptor = stringEncryptor;
        this.languageUtils = languageUtils;
        this.edsAssetService = edsAssetService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        AliyunKmsModel.CreateSecret createSecret = param.getDetail();
        // 校验Secret是否存在
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        Optional<DescribeSecretResponseBody> optionalDescribeSecretResponseBody = AliyunKmsRepo.describeSecret(
                createSecret.getEndpoint(), aliyun, createSecret.getSecretName());
        if (optionalDescribeSecretResponseBody.isPresent()) {
            WorkOrderTicketException.runtime(
                    languageUtils.getFormattedMessage("workorder.ticket.aliyun.kms.secret.create.verify",
                            createSecret.getSecretName(), createSecret.getVersionId()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunKmsModel.CreateSecret createSecret) throws WorkOrderTicketException {
        WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry param = WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry.builder()
                .detail(createSecret)
                .build();
        // 再次验证，避免重复申请
        this.verifyEntryParam(param, entry);
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        Map<String, String> tags = buildTags(workOrderTicket, entry);
        // 解密 Secret 数据
        String secretData = stringEncryptor.decrypt(createSecret.getSecretData());
        try {
            Optional<CreateSecretResponseBody> optionalCreateSecretResponseBody = AliyunKmsRepo.createSecret(
                    createSecret.getEndpoint(), aliyun, createSecret.getKmsInstance()
                            .getAssetId(), createSecret.getSecretName(), createSecret.getVersionId(),
                    createSecret.getEncryptionKeyId(), secretData, tags, createSecret.getDescription());
            if (optionalCreateSecretResponseBody.isEmpty()) {
                WorkOrderTicketException.runtime("Failed to create KMS secret: " + createSecret.getSecretName());
            }
            Optional<DescribeSecretResponseBody> optionalDescribeSecretResponseBody = AliyunKmsRepo.describeSecret(
                    createSecret.getEndpoint(), aliyun, createSecret.getSecretName());
            if (optionalDescribeSecretResponseBody.isEmpty()) {
                WorkOrderTicketException.runtime("Failed to describe KMS secret: " + createSecret.getSecretName());
            }
            // 写入资产
            importAsset(holder, createSecret, optionalDescribeSecretResponseBody.get(), tags);
        } catch (Exception e) {
            WorkOrderTicketException.runtime("Failed to create KMS secret err: " + e.getMessage());
        }
    }

    private Map<String, String> buildTags(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry) {
        Map<String, String> tags = Maps.newHashMap();
        tags.put(SysTagKeys.CREATED_BY.getKey(), workOrderTicket.getUsername());
        tags.put(SysTagKeys.ENV.getKey(), entry.getNamespace());
        return tags;
    }

    /**
     * 写入资产
     *
     * @param holder
     * @param createSecret
     * @param describeSecretResponseBody
     */
    private void importAsset(EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder,
                             AliyunKmsModel.CreateSecret createSecret,
                             DescribeSecretResponseBody describeSecretResponseBody, Map<String, String> tags) {
        try {
            AliyunKms.SecretMetadata metadata = BeanCopierUtils.copyProperties(describeSecretResponseBody,
                    AliyunKms.SecretMetadata.class);
            metadata.setTags(AliyunKms.Tags.of(tags));
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
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
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
        WorkOrderTicket ticket = workOrderTicketService.getById(addCreateAliyunKmsSecretTicketEntry.getTicketId());
        String username = ticket.getUsername();
        EdsAssetIndex endpointIndex = edsAssetIndexService.getByAssetIdAndName(
                addCreateAliyunKmsSecretTicketEntry.getDetail()
                        .getKmsInstance()
                        .getId(), ALIYUN_KMS_ENDPOINT);
        String secretData = Optional.of(addCreateAliyunKmsSecretTicketEntry)
                .map(WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry::getDetail)
                .map(AliyunKmsModel.CreateSecret::getSecretData)
                .orElseThrow(() -> new WorkOrderTicketException("Secret data is required"));
        // 信息摘要
        String contentHash = InfoSummaryUtils.toContentHash(InfoSummaryUtils.SHA256,
                InfoSummaryUtils.toSHA256(secretData));

        List<EdsAssetIndex> contentHashIndices = edsAssetIndexService.queryInstanceIndexByNameAndValue(instanceId,
                CONTENT_HASH, contentHash);

        List<EdsAsset> duplicateSecretAssets = contentHashIndices.stream()
                .map(index -> edsAssetService.getById(index.getAssetId()))
                .filter(Objects::nonNull)
                .toList();
        // 加密 Secret 数据
        String encryptedSecretData = stringEncryptor.encrypt(secretData);
        return AliyunKmsSecretCreateTicketEntryBuilder.newBuilder()
                .withParam(addCreateAliyunKmsSecretTicketEntry)
                .withEdsInstance(BeanCopierUtils.copyProperties(instance, EdsInstanceVO.EdsInstance.class))
                .withUsername(username)
                .withEndpoint(endpointIndex.getValue())
                .withEncryptedSecretData(encryptedSecretData)
                .withDuplicateSecretAssets(duplicateSecretAssets)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aliyun Instance | Secret Name | Version ID | Encryption Key ID | Config Center Value | Duplicate Secret | Description |
                | --- | --- | --- | --- | --- | --- | --- |
                """;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunKmsModel.CreateSecret createSecret = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), createSecret.getSecretName(),
                createSecret.getVersionId(), createSecret.getEncryptionKeyId(), "KMS#" + createSecret.getSecretName(),
                createSecret.getDuplicateSecrets(), createSecret.getDescription());
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
