package com.baiyi.cratos.workorder.entry.impl.aws;

import com.amazonaws.services.transfer.model.HomeDirectoryMapEntry;
import com.amazonaws.services.transfer.model.ListedUser;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.SshKeyUtils;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AwsTransferModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.aws.model.AwsTransferServer;
import com.baiyi.cratos.eds.aws.repo.AwsTransferRepo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
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
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.baiyi.cratos.workorder.entry.impl.aws.AwsTransferSftpUserPermissionTicketEntryProvider.ConfigMap.ROLE;
import static com.baiyi.cratos.workorder.entry.impl.aws.AwsTransferSftpUserPermissionTicketEntryProvider.ConfigMap.TARGET;

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

    private final EdsInstanceService edsInstanceService;
    private final BusinessTagService businessTagService;
    private final TagService tagService;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final BusinessTagFacade businessTagFacade;

    interface ConfigMap {
        String ROLE = "role";
        String TARGET = "target";
    }

    public AwsTransferSftpUserPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                            WorkOrderTicketService workOrderTicketService,
                                                            WorkOrderService workOrderService,
                                                            EdsInstanceService edsInstanceService,
                                                            BusinessTagService businessTagService,
                                                            TagService tagService, EdsAssetService edsAssetService,
                                                            EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                            BusinessTagFacade businessTagFacade) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.businessTagService = businessTagService;
        this.tagService = tagService;
        this.edsAssetService = edsAssetService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.businessTagFacade = businessTagFacade;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AwsTransferModel.SFTPUser sftpUser) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, AwsTransferServer.TransferServer> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, AwsTransferServer.TransferServer>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.AWS_TRANSFER_SERVER.name());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        Map<String, String> configMapData = getConfigMapData(sftpUser);
        final String target = StringFormatter.format(configMapData.get(TARGET), sftpUser.getUsername());
        Collection<HomeDirectoryMapEntry> homeDirectoryMappings = AwsTransferRepo.generateHomeDirectoryMappings(target);
        // createUser(String regionId, EdsAwsConfigModel.Aws aws, Collection<HomeDirectoryMapEntry> homeDirectoryMappings, String userName, String role, String serverId, String sshPublicKey)
        String regionId = sftpUser.getAsset()
                .getRegion();
        AwsTransferRepo.createUser(regionId, aws, homeDirectoryMappings, sftpUser.getUsername(),
                configMapData.get(ROLE), sftpUser.getAsset()
                        .getAssetId(), sftpUser.getPublicKey());
        // import transfer server
        try {
            AwsTransferServer.TransferServer transferServer = holder.getProvider()
                    .assetLoadAs(sftpUser.getAsset()
                            .getOriginalModel());
            List<ListedUser> sftpUsers = AwsTransferRepo.listUsers(regionId, aws, sftpUser.getAsset()
                    .getAssetId());
            transferServer.setUsers(sftpUsers);
            holder.importAsset(transferServer);
        } catch (Exception e) {
            log.warn("Failed to import AWS Transfer Server: {}", e.getMessage());
        }
    }

    private Map<String, String> getConfigMapData(AwsTransferModel.SFTPUser sftpUser) {
        BaseBusiness.HasBusiness hasBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(sftpUser.getAsset()
                        .getId())
                .build();
        Map<String, String> configMapData = businessTagFacade.getConfigMapData(hasBusiness);
        if (CollectionUtils.isEmpty(configMapData)) {
            return Map.of();
        }
        return configMapData;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        // 校验用户名是否合规
        String username = Optional.ofNullable(param)
                .map(WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry::getDetail)
                .map(AwsTransferModel.SFTPUser::getUsername)
                .orElseThrow(() -> new WorkOrderTicketException("AWS Transfer Server username is null"));
        if (!ValidationUtils.isTransferServerUsername(username)) {
            throw new WorkOrderTicketException("Invalid AWS Transfer Server username: " + username);
        }
        // 校验用户名是否冲突
        AwsTransferModel.SFTPUser sftpUser = Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateAwsTransferSftpUserTicketEntry::getDetail)
                .orElseThrow(() -> new WorkOrderTicketException("SFTP user detail is null"));
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, AwsTransferServer.TransferServer> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, AwsTransferServer.TransferServer>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.AWS_TRANSFER_SERVER.name());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        List<ListedUser> transferUsers = AwsTransferRepo.listUsers(sftpUser.getAsset()
                .getRegion(), aws, sftpUser.getAsset()
                .getAssetId());
        if (!CollectionUtils.isEmpty(transferUsers) && transferUsers.stream()
                .anyMatch(u -> username.equals(u.getUserName()))) {
            throw new WorkOrderTicketException("AWS Transfer Server username already exists: " + username);
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
        return MarkdownUtils.createTableHeader(TableHeaderConstants.AWS_TRANSFER_SFTP_USER_PERMISSION);
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AwsTransferModel.SFTPUser sftpUser = loadAs(entry);
        int instanceId = Optional.of(sftpUser)
                .map(AwsTransferModel.SFTPUser::getAsset)
                .map(EdsAssetVO.Asset::getInstanceId)
                .orElse(0);
        String instanceName = IdentityUtils.hasIdentity(instanceId) ? edsInstanceService.getById(instanceId)
                .getInstanceName() : Global.NONE;
        String endpoint = sftpUser.getTransferServerEndpoint();
        String transferLogin = Joiner.on("@")
                .join(sftpUser.getUsername(), endpoint);
        return MarkdownUtils.createTableRow(instanceName, transferLogin,
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
        return Global.NONE;
    }

}
