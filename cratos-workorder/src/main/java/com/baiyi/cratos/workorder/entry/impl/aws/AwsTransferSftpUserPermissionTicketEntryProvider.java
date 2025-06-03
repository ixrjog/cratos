package com.baiyi.cratos.workorder.entry.impl.aws;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.SshKeyUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AwsTransferModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.CreateAwsTransferSftpUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/3 09:56
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.AWS_TRANSFER_SFTP_USER_PERMISSION)
public class AwsTransferSftpUserPermissionTicketEntryProvider extends BaseTicketEntryProvider<AwsTransferModel.SFTPUser, WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry> {

    private static final String ROW_TPL = "| {} | {} | {} | {} |";
    private final EdsInstanceService edsInstanceService;
    private final BusinessTagService businessTagService;
    private final TagService tagService;
    private final EdsAssetService edsAssetService;

    public AwsTransferSftpUserPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                            WorkOrderTicketService workOrderTicketService,
                                                            WorkOrderService workOrderService,
                                                            EdsInstanceService edsInstanceService,
                                                            BusinessTagService businessTagService,
                                                            TagService tagService, EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.businessTagService = businessTagService;
        this.tagService = tagService;
        this.edsAssetService = edsAssetService;
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AwsTransferModel.SFTPUser sftpUser) throws WorkOrderTicketException {
        // TODO
    }

    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        String username = Optional.ofNullable(param)
                .map(WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry::getDetail)
                .map(AwsTransferModel.SFTPUser::getUsername)
                .orElseThrow(() -> new WorkOrderTicketException("AWS Transfer Server username is null"));
        if (!ValidationUtils.isTransferServerUsername(username)) {
            WorkOrderTicketException.runtime("Invalid AWS Transfer Server username: " + username);
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry addCreateAwsTransferSftpUserTicketEntry) {
        int assetId = Optional.ofNullable(addCreateAwsTransferSftpUserTicketEntry)
                .map(WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry::getDetail)
                .map(AwsTransferModel.SFTPUser::getAsset)
                .map(EdsAssetVO.Asset::getId)
                .orElseThrow(() -> new WorkOrderTicketException("AWS TransferServer asset is null"));
        EdsAsset edsAsset = edsAssetService.getById(assetId);
        Objects.requireNonNull(edsAsset, "AWS TransferServer asset not found");
        if (!EdsAssetTypeEnum.AWS_TRANSFER_SERVER.name()
                .equals(edsAsset.getAssetType())) {
            WorkOrderTicketException.runtime("AWS TransferServer asset type is not AWS_TRANSFER_SERVER");
        }
        String endpoint = getTransferServerEndpoint(addCreateAwsTransferSftpUserTicketEntry.getDetail());
        return CreateAwsTransferSftpUserTicketEntryBuilder.newBuilder()
                .withParam(addCreateAwsTransferSftpUserTicketEntry)
                .withSftpUser(addCreateAwsTransferSftpUserTicketEntry.getDetail())
                .withTransferServerEndpoint(endpoint)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aws Instance | Transfer Username@Server | Key Fingerprint | Desc |
                | --- | --- | --- | --- |
                """;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AwsTransferModel.SFTPUser sftpUser = loadAs(entry);
        int instanceId = Optional.of(sftpUser)
                .map(AwsTransferModel.SFTPUser::getAsset)
                .map(EdsAssetVO.Asset::getInstanceId)
                .orElse(0);
        String instanceName = IdentityUtil.hasIdentity(instanceId) ? edsInstanceService.getById(instanceId)
                .getInstanceName() : "--";
        String endpoint = sftpUser.getTransferServerEndpoint();
        String transferLogin = Joiner.on("@")
                .join(sftpUser.getUsername(), endpoint);
        return StringFormatter.arrayFormat(ROW_TPL, instanceName, transferLogin,
                SshKeyUtils.calcFingerprint(sftpUser.getPublicKey()), sftpUser.getDescription());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        AwsTransferModel.SFTPUser sftpUser = loadAs(entry);
        String endpoint = sftpUser.getTransferServerEndpoint();
        String transferLogin = Joiner.on("@")
                .join(sftpUser.getUsername(), endpoint);
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Transfer User")
                .build();
    }

    private String getTransferServerEndpoint(AwsTransferModel.SFTPUser sftpUser) {
        Tag endpointTag = tagService.getByTagKey(SysTagKeys.ENDPOINT);
        if (Objects.nonNull(endpointTag)) {
            BusinessTag uk = BusinessTag.builder()
                    .tagId(endpointTag.getId())
                    .businessId(sftpUser.getAsset()
                            .getId())
                    .businessType(sftpUser.getAsset()
                            .getBusinessType())
                    .build();
            BusinessTag businessTag = businessTagService.getByUniqueKey(uk);
            return businessTag.getTagValue();
        }
        return "--";
    }

}
