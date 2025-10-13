package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyun.sdk.service.kms20160120.models.DescribeSecretResponseBody;
import com.aliyun.sdk.service.kms20160120.models.PutSecretValueResponseBody;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunKmsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aliyun.model.AliyunKms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunKmsRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.AliyunKmsSecretUpdateTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_KMS_ENDPOINT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/9 13:48
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_KMS_SECRET_UPDATE)
public class AliyunKmsSecretUpdateTicketEntryProvider extends BaseTicketEntryProvider<AliyunKmsModel.UpdateSecret, WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsAssetIndexService edsAssetIndexService;
    private final StringEncryptor stringEncryptor;
    private final LanguageUtils languageUtils;

    public AliyunKmsSecretUpdateTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                    WorkOrderTicketService workOrderTicketService,
                                                    WorkOrderService workOrderService,
                                                    EdsInstanceService edsInstanceService,
                                                    EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                    EdsAssetIndexService edsAssetIndexService,
                                                    StringEncryptor stringEncryptor, LanguageUtils languageUtils) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsAssetIndexService = edsAssetIndexService;
        this.stringEncryptor = stringEncryptor;
        this.languageUtils = languageUtils;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        AliyunKmsModel.UpdateSecret updateSecret = param.getDetail();
        // 校验Secret是否存在
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        Optional<DescribeSecretResponseBody> optionalDescribeSecretResponseBody = AliyunKmsRepo.describeSecret(
                updateSecret.getEndpoint(), aliyun, updateSecret.getSecretName());
        if (optionalDescribeSecretResponseBody.isPresent()) {
            // 如果存在则查询 versionId 是否冲突
            boolean versionConflict = AliyunKmsRepo.listSecretVersionIds(updateSecret.getEndpoint(), aliyun,
                            updateSecret.getSecretName())
                    .stream()
                    .anyMatch(e -> updateSecret.getVersionId()
                            .equals(e.getVersionId()));
            if (versionConflict) {
                WorkOrderTicketException.runtime(
                        languageUtils.getFormattedMessage("workorder.ticket.aliyun.kms.secret.update.verify",
                                updateSecret.getSecretName(), updateSecret.getVersionId()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunKmsModel.UpdateSecret updateSecret) throws WorkOrderTicketException {
        WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry param = WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry.builder()
                .detail(updateSecret)
                .build();
        // 再次验证，避免重复申请versionId冲突
        this.verifyEntryParam(param, entry);
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, AliyunKms.KmsSecret>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_KMS_SECRET.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        // 解密 Secret 数据
        String secretData = stringEncryptor.decrypt(updateSecret.getSecretData());
        Optional<PutSecretValueResponseBody> optionalPutSecretValueResponseBody = AliyunKmsRepo.putSecretValue(
                updateSecret.getEndpoint(), aliyun, updateSecret.getSecretName(), updateSecret.getVersionId(),
                secretData);
        if (optionalPutSecretValueResponseBody.isEmpty()) {
            WorkOrderTicketException.runtime("Failed to update KMS secret value: " + updateSecret.getSecretName());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry addUpdateAliyunKmsSecretTicketEntry) {
        int instanceId = Optional.ofNullable(addUpdateAliyunKmsSecretTicketEntry)
                .map(WorkOrderTicketParam.TicketEntry::getInstanceId)
                .orElseThrow(() -> new WorkOrderTicketException("Instance ID is required"));
        if (!IdentityUtils.hasIdentity(instanceId)) {
            WorkOrderTicketException.runtime("Invalid instance ID: " + instanceId);
        }
        EdsInstance instance = edsInstanceService.getById(instanceId);
        if (instance == null) {
            WorkOrderTicketException.runtime("Instance not found: " + instanceId);
        }
        WorkOrderTicket ticket = workOrderTicketService.getById(addUpdateAliyunKmsSecretTicketEntry.getTicketId());
        String username = ticket.getUsername();
        EdsAssetIndex endpointIndex = edsAssetIndexService.getByAssetIdAndName(
                addUpdateAliyunKmsSecretTicketEntry.getDetail()
                        .getSecret()
                        .getId(), ALIYUN_KMS_ENDPOINT);
        // 加密 Secret 数据
        String encryptedSecretData = stringEncryptor.encrypt(Optional.of(addUpdateAliyunKmsSecretTicketEntry)
                .map(WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry::getDetail)
                .map(AliyunKmsModel.UpdateSecret::getSecretData)
                .orElseThrow(() -> new WorkOrderTicketException("Secret data is required")));
        return AliyunKmsSecretUpdateTicketEntryBuilder.newBuilder()
                .withParam(addUpdateAliyunKmsSecretTicketEntry)
                .withEdsInstance(BeanCopierUtils.copyProperties(instance, EdsInstanceVO.EdsInstance.class))
                .withEndpoint(endpointIndex.getValue())
                .withEncryptedSecretData(encryptedSecretData)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.generateMarkdownSeparator(
                "| Aliyun Instance | Secret Name | Version ID | Config Center Value |");
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunKmsModel.UpdateSecret updateSecret = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.generateMarkdownTableRow(instance.getInstanceName(), updateSecret.getSecretName(),
                updateSecret.getVersionId(), "KMS#" + updateSecret.getSecretName());
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
