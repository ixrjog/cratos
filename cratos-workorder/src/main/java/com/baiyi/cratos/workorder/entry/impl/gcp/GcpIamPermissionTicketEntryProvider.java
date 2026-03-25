package com.baiyi.cratos.workorder.entry.impl.gcp;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.GcpModel;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import com.baiyi.cratos.eds.googlecloud.model.GcpMemberModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpProjectRepo;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.GcpIamMemberTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.GcpIamPermissionNoticeSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/24 10:28
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.GCP_IAM_PERMISSION)
public class GcpIamPermissionTicketEntryProvider extends BaseTicketEntryProvider<GcpModel.IamMember, WorkOrderTicketParam.AddGcpIamMemberTicketEntry> {

    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final BusinessTagFacade businessTagFacade;
    private final TagService tagService;
    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final GcpProjectRepo gcpProjectRepo;
    private final GcpIamPermissionNoticeSender gcpIamPermissionNoticeSender;

    private static final String DEF_ROLE = "roles/browser";

    public GcpIamPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                               WorkOrderTicketService workOrderTicketService,
                                               WorkOrderService workOrderService,
                                               EdsProviderHolderFactory edsProviderHolderFactory,
                                               BusinessTagFacade businessTagFacade, TagService tagService,
                                               UserService userService, GcpProjectRepo gcpProjectRepo,
                                               GcpIamPermissionNoticeSender gcpIamPermissionNoticeSender) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsProviderHolderFactory = edsProviderHolderFactory;
        this.businessTagFacade = businessTagFacade;
        this.tagService = tagService;
        this.userService = userService;
        this.workOrderService = workOrderService;
        this.gcpProjectRepo = gcpProjectRepo;
        this.gcpIamPermissionNoticeSender = gcpIamPermissionNoticeSender;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.GCP_IAM_PERMISSION);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                GcpModel.IamMember iamMember) throws WorkOrderTicketException {
        // GCP IAM Member
        EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member>) edsProviderHolderFactory.createHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsConfigs.Gcp gcp = holder.getInstance()
                .getConfig();
        try {
            gcpProjectRepo.addMember(gcp, GcpIAMMemberType.USER, iamMember.getMember(), DEF_ROLE);
            // 发送通知
            sendMsg(workOrderTicket, iamMember);
            // 导入资产
            GcpMemberModel.Member member = GcpMemberModel.Member.builder()
                    .type(GcpIAMMemberType.USER.getKey())
                    .name(iamMember.getMember())
                    .roles(List.of(DEF_ROLE))
                    .build();
            EdsAsset asset = holder.importAsset(member);
            // 关联用户
            addUsernameTag(asset, iamMember.getUsername());
        } catch (IOException ioException) {
            WorkOrderTicketException.runtime(ioException.getMessage(), ioException);
        }
    }

    private void sendMsg(WorkOrderTicket workOrderTicket, GcpModel.IamMember iamMember) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(iamMember.getUsername());
            gcpIamPermissionNoticeSender.sendMsg(workOrder, workOrderTicket, iamMember, applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    private void addUsernameTag(EdsAsset asset, String username) {
        // 标签
        Tag usernameTag = tagService.getByTagKey(SysTagKeys.USERNAME);
        if (Objects.isNull(usernameTag)) {
            return;
        }
        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                .tagId(usernameTag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(username)
                .build();
        businessTagFacade.saveBusinessTag(saveBusinessTag);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddGcpIamMemberTicketEntry param) {
        GcpModel.IamMember detail = Optional.ofNullable(param.getDetail())
                .orElseThrow(() -> new WorkOrderTicketException("Detail is null"));
        int instanceId = Optional.ofNullable(detail.getEdsInstance())
                .map(EdsInstanceVO.EdsInstance::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Eds instanceId is null"));
        String member = Optional.ofNullable(detail.getMember())
                .orElseThrow(() -> new WorkOrderTicketException("Member is null"));
        // Project
        EdsInstanceProviderHolder<EdsConfigs.Gcp, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, ?>) edsProviderHolderFactory.createHolder(
                instanceId, EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsGcpConfigModel.Project project = Optional.ofNullable(holder.getInstance().getConfig())
                .map(EdsConfigs.Gcp::getProject)
                .orElseThrow(() -> new WorkOrderTicketException("Project is null"));
        String projectId = Optional.ofNullable(project.getId())
                .orElseThrow(() -> new WorkOrderTicketException("Project id is null"));
        String projectName = Optional.ofNullable(project.getName()).orElse("--");
        return GcpIamMemberTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .withMember(member)
                .withProjectId(projectId)
                .withProjectName(projectName)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        GcpModel.IamMember iamMember = loadAs(entry);
        EdsInstanceProviderHolder<EdsConfigs.Gcp, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, ?>) edsProviderHolderFactory.createHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsGcpConfigModel.Project project = Optional.ofNullable(holder.getInstance().getConfig())
                .map(EdsConfigs.Gcp::getProject)
                .orElse(null);
        String projectName = project != null ? Optional.ofNullable(project.getName()).orElse("--") : "--";
        String projectId = project != null ? Optional.ofNullable(project.getId()).orElse("--") : "--";
        return MarkdownUtils.createTableRow(projectName, projectId, iamMember.getMember(), iamMember.getUsername());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("IAM Member")
                .build();
    }

}
