package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunKmsModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/20 11:15
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunKmsSecretCreateTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry param;
    private EdsInstanceVO.EdsInstance edsInstance;
    private String username;

    public static AliyunKmsSecretCreateTicketEntryBuilder newBuilder() {
        return new AliyunKmsSecretCreateTicketEntryBuilder();
    }

    public AliyunKmsSecretCreateTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateAliyunKmsSecretTicketEntry param) {
        this.param = param;
        return this;
    }

    public AliyunKmsSecretCreateTicketEntryBuilder withEdsInstance(EdsInstanceVO.EdsInstance edsInstance) {
        this.edsInstance = edsInstance;
        return this;
    }

    public AliyunKmsSecretCreateTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        AliyunKmsModel.CreateSecret detail = param.getDetail();
        detail.setEdsInstance(edsInstance);
        String baseDescription = StringUtils.hasText(detail.getDescription()) ? detail.getDescription() + " | " : "";
        detail.setDescription(baseDescription + "Created by " + username);
        if (!StringUtils.hasText(detail.getVersionId())) {
            detail.setVersionId("v1");
        }
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
