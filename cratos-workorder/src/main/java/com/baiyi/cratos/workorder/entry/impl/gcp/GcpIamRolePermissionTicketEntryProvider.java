package com.baiyi.cratos.workorder.entry.impl.gcp;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.GcpModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import com.baiyi.cratos.eds.googlecloud.model.GcpMemberModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpProjectRepo;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.GcpIamRoleTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/24 13:30
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.GCP_IAM_ROLE_PERMISSION)
public class GcpIamRolePermissionTicketEntryProvider extends BaseTicketEntryProvider<GcpModel.MemberRole, WorkOrderTicketParam.AddGcpIamRoleTicketEntry> {

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final GcpProjectRepo gcpProjectRepo;
    private final EdsAssetService edsAssetService;

    public GcpIamRolePermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService,
                                                   EdsProviderHolderFactory edsProviderHolderFactory,
                                                   GcpProjectRepo gcpProjectRepo, EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsProviderHolderFactory = edsProviderHolderFactory;
        this.gcpProjectRepo = gcpProjectRepo;
        this.edsAssetService = edsAssetService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.GCP_IAM_ROLE_PERMISSION);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                GcpModel.MemberRole memberRole) throws WorkOrderTicketException {
        // GCP IAM Member
        EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member>) edsProviderHolderFactory.createHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsConfigs.Gcp gcp = holder.getInstance()
                .getConfig();
        try {
            gcpProjectRepo.grantRole(
                    gcp, GcpIAMMemberType.USER, memberRole.getMember(), memberRole.getRole()
                            .getName()
            );
        } catch (IOException ioException) {
            WorkOrderTicketException.runtime(ioException.getMessage(), ioException);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddGcpIamRoleTicketEntry param) {
        int assetId = Optional.of(param)
                .map(WorkOrderTicketParam.AddGcpIamRoleTicketEntry::getDetail)
                .map(GcpModel.MemberRole::getAsset)
                .map(EdsAssetVO.Asset::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Asset id is null"));
        EdsAsset asset = edsAssetService.getById(assetId);
        int instanceId = asset.getInstanceId();
        String member = Optional.of(param)
                .map(WorkOrderTicketParam.AddGcpIamRoleTicketEntry::getDetail)
                .map(GcpModel.MemberRole::getMember)
                .orElseThrow(() -> new WorkOrderTicketException("Member is null"));
        EdsInstanceProviderHolder<EdsConfigs.Gcp, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, ?>) edsProviderHolderFactory.createHolder(
                instanceId, EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsConfigs.Gcp gcp = holder.getInstance()
                .getConfig();
        String projectId = Optional.of(gcp)
                .map(EdsConfigs.Gcp::getProject)
                .map(EdsGcpConfigModel.Project::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Project id is null"));
        String projectName = Optional.of(gcp)
                .map(EdsConfigs.Gcp::getProject)
                .map(EdsGcpConfigModel.Project::getName)
                .orElse("--");

        String roleName = Optional.of(param)
                .map(WorkOrderTicketParam.AddGcpIamRoleTicketEntry::getDetail)
                .map(GcpModel.MemberRole::getRole)
                .map(GcpModel.IamRole::getName)
                .orElseThrow(() -> new WorkOrderTicketException("Role is null"));
        if (!StringUtils.hasText(roleName)) {
            WorkOrderTicketException.runtime("Role is null");
        }
        // 查找 GCP IAM Role
        List<EdsAsset> assets = edsAssetService.queryInstanceAssetByTypeAndKey(
                instanceId, EdsAssetTypeEnum.GCP_IAM_ROLE.name(), roleName);
        if (assets.size() != 1) {
            WorkOrderTicketException.runtime("Role name is error: " + roleName);
        }
        GcpModel.IamRole iamRole = GcpModel.IamRole.builder()
                .title(assets.getFirst()
                               .getName())
                .name(assets.getFirst()
                              .getAssetKey())
                .description(assets.getFirst()
                                     .getDescription())
                .build();
        return GcpIamRoleTicketEntryBuilder.newBuilder()
                .withUsername(SessionUtils.getUsername())
                .withParam(param)
                .withMember(member)
                .withProjectId(projectId)
                .withProjectName(projectName)
                .withIamRole(iamRole)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        GcpModel.MemberRole memberRole = loadAs(entry);
        EdsInstanceProviderHolder<EdsConfigs.Gcp, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, ?>) edsProviderHolderFactory.createHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsConfigs.Gcp gcp = holder.getInstance()
                .getConfig();
        String projectName = Optional.of(gcp)
                .map(EdsConfigs.Gcp::getProject)
                .map(EdsGcpConfigModel.Project::getName)
                .orElse("--");
        String projectId = Optional.of(gcp)
                .map(EdsConfigs.Gcp::getProject)
                .map(EdsGcpConfigModel.Project::getId)
                .orElse("--");
        // "| Google Cloud Project Name | Google Cloud Project ID | IAM Member (Google Account) | Role Title |";
        return MarkdownUtils.createTableRow(
                projectName, projectId, memberRole.getMember(), memberRole.getRole()
                        .getTitle()
        );
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("IAM Role")
                .build();
    }

}
