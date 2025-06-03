package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.SshKeyUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AwsTransferModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.google.common.base.Joiner;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/3 11:18
 * &#064;Version 1.0
 */
public class CreateAwsTransferSftpUserTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry param;
    private AwsTransferModel.SFTPUser sftpUser;
    private String transferServerEndpoint;

    public static CreateAwsTransferSftpUserTicketEntryBuilder newBuilder() {
        return new CreateAwsTransferSftpUserTicketEntryBuilder();
    }

    public CreateAwsTransferSftpUserTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry param) {
        this.param = param;
        return this;
    }

    public CreateAwsTransferSftpUserTicketEntryBuilder withSftpUser(AwsTransferModel.SFTPUser sftpUser) {
        this.sftpUser = sftpUser;
        return this;
    }

    public CreateAwsTransferSftpUserTicketEntryBuilder withTransferServerEndpoint(String transferServerEndpoint) {
        this.transferServerEndpoint = transferServerEndpoint;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        String fingerprint = SshKeyUtils.calcFingerprint(sftpUser.getPublicKey());
        sftpUser.setTransferServerEndpoint(transferServerEndpoint);
        sftpUser.setKeyFingerprint(fingerprint);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(sftpUser.getUsername())
                .displayName(Joiner.on("@")
                        .join(sftpUser.getUsername(), transferServerEndpoint))
                .instanceId(sftpUser.getAsset()
                        .getInstanceId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.AWS_TRANSFER_SERVER.name())
                .businessId(sftpUser.getAsset()
                        .getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:sftpUsername:{}", sftpUser.getAsset()
                        .getInstanceId(), sftpUser.getUsername()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .comment(sftpUser.getDescription())
                .build();
    }

}
