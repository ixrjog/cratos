package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunKmsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/10 10:35
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunKmsSecretUpdateTicketEntryBuilder {

    private WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry param;
    private EdsInstanceVO.EdsInstance edsInstance;
    private String endpoint;
    private String encryptedSecretData;

    public static AliyunKmsSecretUpdateTicketEntryBuilder newBuilder() {
        return new AliyunKmsSecretUpdateTicketEntryBuilder();
    }

    public AliyunKmsSecretUpdateTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddUpdateAliyunKmsSecretTicketEntry param) {
        this.param = param;
        return this;
    }

    public AliyunKmsSecretUpdateTicketEntryBuilder withEdsInstance(EdsInstanceVO.EdsInstance edsInstance) {
        this.edsInstance = edsInstance;
        return this;
    }

    public AliyunKmsSecretUpdateTicketEntryBuilder withEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public AliyunKmsSecretUpdateTicketEntryBuilder withEncryptedSecretData(String encryptedSecretData) {
        this.encryptedSecretData = encryptedSecretData;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        AliyunKmsModel.UpdateSecret detail = param.getDetail();
        detail.setSecretName(detail.getSecret()
                .getName());
        detail.setEdsInstance(edsInstance);
        detail.setEndpoint(endpoint);
        detail.setSecretData(encryptedSecretData);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(detail.getSecretName())
                .displayName("KMS#" + detail.getSecretName())
                .instanceId(edsInstance.getId())
                .businessType(param.getBusinessType())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:secretName:{}:versionId:{}", edsInstance.getId(),
                        detail.getSecretName(), detail.getVersionId()))
                .namespace(param.getNamespace())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
