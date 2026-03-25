package com.baiyi.cratos.workorder.entry.impl.gcp;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.GcpModel;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
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
    private final EdsIdentityFacade edsIdentityFacade;
//    @Autowired
//    private EdsCloudIdentityExtension edsCloudIdentityExtension;

    public GcpIamRolePermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService,
                                                   EdsProviderHolderFactory edsProviderHolderFactory,
                                                   GcpProjectRepo gcpProjectRepo, EdsAssetService edsAssetService,EdsIdentityFacade edsIdentityFacade) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsProviderHolderFactory = edsProviderHolderFactory;
        this.gcpProjectRepo = gcpProjectRepo;
        this.edsAssetService = edsAssetService;
        this.edsIdentityFacade = edsIdentityFacade;
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
        GcpModel.MemberRole detail = Optional.ofNullable(param.getDetail())
                .orElseThrow(() -> new WorkOrderTicketException("Detail is null"));
        // Asset
        int roleAssetId = Optional.ofNullable(detail.getAsset())
                .map(EdsAssetVO.Asset::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Asset id is null"));
        // Role
        EdsAsset roleAsset = edsAssetService.getById(roleAssetId);
        if (roleAsset == null || !EdsAssetTypeEnum.GCP_IAM_ROLE.name()
                .equals(roleAsset.getAssetType())) {
            WorkOrderTicketException.runtime("角色不存在或类型不正确");
        }

        int instanceId = roleAsset.getInstanceId();
        // Member
        String username = SessionUtils.getUsername();
        EdsIdentityParam.QueryCloudIdentityDetails query =   EdsIdentityParam.QueryCloudIdentityDetails.builder()
                .instanceId(instanceId)
                .instanceType(EdsInstanceTypeEnum.GCP.name())
                .username(username)
                .build();
        EdsIdentityVO.CloudIdentityDetails cloudIdentityDetails = edsIdentityFacade.queryCloudIdentityDetails(query);
        if(!cloudIdentityDetails.getAccounts().containsKey(EdsInstanceTypeEnum.GCP.name())){
            WorkOrderTicketException.runtime("Google Cloud 账户不存在.");
        }
        List<EdsIdentityVO.CloudAccount> gcpAccounts = cloudIdentityDetails.getAccounts().get(EdsInstanceTypeEnum.GCP.name());
        if(gcpAccounts.size() != 1){
            WorkOrderTicketException.runtime("Google Cloud 账户配置不正确.");
        }
        EdsIdentityVO.CloudAccount gcpAccount = gcpAccounts.getFirst();
        String member = gcpAccount.getUsername();
        // Project
        EdsInstanceProviderHolder<EdsConfigs.Gcp, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, ?>) edsProviderHolderFactory.createHolder(
                instanceId, EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsGcpConfigModel.Project project = Optional.ofNullable(holder.getInstance()
                                                                        .getConfig())
                .map(EdsConfigs.Gcp::getProject)
                .orElseThrow(() -> new WorkOrderTicketException("Project is null"));
        String projectId = Optional.ofNullable(project.getId())
                .orElseThrow(() -> new WorkOrderTicketException("Project id is null"));
        String projectName = Optional.ofNullable(project.getName())
                .orElse("--");
        GcpModel.IamRole iamRole = GcpModel.IamRole.builder()
                .title(roleAsset.getName())
                .name(roleAsset.getAssetKey())
                .description(roleAsset.getDescription())
                .build();
        return GcpIamRoleTicketEntryBuilder.newBuilder()
                .withUsername(username)
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
        EdsGcpConfigModel.Project project = Optional.ofNullable(holder.getInstance()
                                                                        .getConfig())
                .map(EdsConfigs.Gcp::getProject)
                .orElse(null);
        String projectName = project != null ? Optional.ofNullable(project.getName())
                .orElse("--") : "--";
        String projectId = project != null ? Optional.ofNullable(project.getId())
                .orElse("--") : "--";
        return MarkdownUtils.createTableRow(projectName, projectId, memberRole.getMember(), memberRole.getRole()
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
