package com.baiyi.cratos.workorder.entry.impl.permission.revoke;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.revoke.*;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.USER_MAIL;

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
    private final EdsIdentityFacade edsIdentityFacade;
    private final EdsAssetIndexService edsAssetIndexService;

    public RevokeUserEdsAccountPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                             WorkOrderTicketService workOrderTicketService,
                                                             WorkOrderService workOrderService,
                                                             EdsInstanceService edsInstanceService,
                                                             EdsIdentityFacade edsIdentityFacade,
                                                             EdsAssetIndexService edsAssetIndexService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsIdentityFacade = edsIdentityFacade;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry addRevokeUserEdsAccountPermissionTicketEntry) {
        EdsAssetVO.Asset asset = addRevokeUserEdsAccountPermissionTicketEntry.getDetail();
        String assetType = asset.getAssetType();
        EdsAssetTypeEnum edsAssetTypeEnum;
        try {
            edsAssetTypeEnum = EdsAssetTypeEnum.valueOf(assetType);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new WorkOrderTicketException("Unsupported asset type: {}", asset.getAssetType());
        }
        return switch (edsAssetTypeEnum) {
            case EdsAssetTypeEnum.LDAP_PERSON -> RevokeUserEdsLdapUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.ALIYUN_RAM_USER -> RevokeUserEdsAliyunRamUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.AWS_IAM_USER -> RevokeUserEdsAwsIamUserPermissionTicketEntryBuilder.newBuilder()
                    .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                    .buildEntry();
            case EdsAssetTypeEnum.AZURE_USER -> RevokeUserEdsAzureUserPermissionTicketEntryBuilder.newBuilder()
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
            case EdsAssetTypeEnum.ALIMAIL_USER -> {
                EdsAssetIndex mailIndex = edsAssetIndexService.getByAssetIdAndName(
                        addRevokeUserEdsAccountPermissionTicketEntry.getDetail()
                                .getId(), USER_MAIL
                );
                if (Objects.isNull(mailIndex)) {
                    throw new WorkOrderTicketException("Assets are missing mail");
                }
                yield RevokeUserEdsAliMailUserPermissionTicketEntryBuilder.newBuilder()
                        .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
                        .withEmail(mailIndex.getValue())
                        .buildEntry();
            }
            default -> throw new WorkOrderTicketException("Unsupported asset type: {}", assetType);
        };
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.REVOKE_USER_ACCOUNT_PERMISSION);
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        EdsAssetVO.Asset asset = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(asset.getInstanceId());
        return MarkdownUtils.createTableRow(
                instance.getInstanceName(), instance.getEdsType(),
                entry.getSubType(), entry.getName()
        );
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
                                EdsAssetVO.Asset detail) throws WorkOrderTicketException {
        String assetType = detail.getAssetType();
        EdsAssetTypeEnum edsAssetTypeEnum = EdsAssetTypeEnum.valueOf(assetType);
        if (EdsAssetTypeEnum.CLOUD_IDENTITY_TYPES
                .contains(edsAssetTypeEnum)) {
            EdsIdentityParam.BlockCloudAccount blockCloudAccount = EdsIdentityParam.BlockCloudAccount.builder()
                    .instanceId(entry.getInstanceId())
                    .account(entry.getName())
                    .accountId(detail.getAssetId())
                    .build();
            // 增加导入资产逻辑
            edsIdentityFacade.blockCloudAccount(blockCloudAccount);
            return;
        }
        // other
        switch (edsAssetTypeEnum) {
            case EdsAssetTypeEnum.LDAP_PERSON -> {
                EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity = EdsIdentityParam.DeleteLdapIdentity.builder()
                        .instanceId(entry.getInstanceId())
                        .username(entry.getName())
                        .build();
                edsIdentityFacade.deleteLdapIdentity(deleteLdapIdentity);
            }
            case EdsAssetTypeEnum.ALIMAIL_USER -> {
                EdsIdentityParam.BlockMailAccount blockMailAccount = EdsIdentityParam.BlockMailAccount.builder()
                        .instanceId(entry.getInstanceId())
                        .userId(detail.getAssetId())
                        .build();
                edsIdentityFacade.blockMailAccount(blockMailAccount);
            }
            case EdsAssetTypeEnum.GITLAB_USER -> {
                EdsIdentityParam.BlockGitLabIdentity blockGitLabIdentity = EdsIdentityParam.BlockGitLabIdentity.builder()
                        .instanceId(entry.getInstanceId())
                        .account(entry.getName())
                        .userId(Long.valueOf(detail.getAssetId()))
                        .build();
                edsIdentityFacade.blockGitLabIdentity(blockGitLabIdentity);
            }
            default -> WorkOrderTicketException.runtime("Unsupported asset type: {}", assetType);
        }
    }

}
