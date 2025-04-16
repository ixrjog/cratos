package com.baiyi.cratos.workorder.entry.impl.revoke;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.revoke.*;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/15 11:18
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.REVOKE_USER_PERMISSION)
public class RevokeUserEdsAccountPermissionTicketEntryProvider extends BaseTicketEntryProvider<EdsAssetVO.Asset, WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> {

    private final EdsInstanceService edsInstanceService;

    public RevokeUserEdsAccountPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                             WorkOrderTicketService workOrderTicketService,
                                                             WorkOrderService workOrderService,
                                                             BusinessTagFacade businessTagFacade,
                                                             EdsInstanceService edsInstanceService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
    }

    private static final String TABLE_TITLE = """
            | Instance Name | Instance Type | Account Type | Account Name |
            | --- | --- | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} | {} | {} |";

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry addRevokeUserEdsAccountPermissionTicketEntry) {
        EdsAssetVO.Asset asset = addRevokeUserEdsAccountPermissionTicketEntry.getDetail();
        String assetType = asset.getAssetType();
        return switch (EdsAssetTypeEnum.valueOf(assetType)) {
            case EdsAssetTypeEnum.LDAP_PERSON -> RevokeUserEdsLdapUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.ALIYUN_RAM_USER -> RevokeUserEdsAliyunRamUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.AWS_IAM_USER -> RevokeUserEdsAwsIamUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.GCP_MEMBER -> RevokeUserEdsGcpMemberPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.HUAWEICLOUD_IAM_USER ->
                    RevokeUserEdsHwcIamUserPermissionTicketEntryBuilder.newBuilder()
                            .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                            .buildEntry();
            case EdsAssetTypeEnum.GITLAB_USER -> RevokeUserEdsGitLabUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            default -> throw new WorkOrderTicketException("不支持的资产类型: " + assetType);
        };
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return TABLE_TITLE;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        EdsAssetVO.Asset asset = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(asset.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), instance.getEdsType(),
                entry.getSubType(), entry.getName());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .desc("Account")
                .build();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                EdsAssetVO.Asset asset) throws WorkOrderTicketException {

    }

}
