package com.baiyi.cratos.domain.param.http.work;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.model.ApplicationReplicasModel;
import com.baiyi.cratos.domain.model.GitLabPermissionModel;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:34
 * &#064;Version 1.0
 */
public class WorkOrderTicketParam {

    public interface HasEntryDetail<T> {
        T getDetail();
    }

    public interface HasTicketNo {
        String getTicketNo();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class MyTicketPageQuery extends PageParam implements HasSessionUser {
        private String ticketNo;
        private String ticketState;
        private String workOrderKey;
        private String username;
        @Schema(description = "我的提交")
        private Boolean mySubmitted;
        private String myUsername;

        @Override
        public void setSessionUser(String username) {
            this.myUsername = username;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class TicketPageQuery extends PageParam {
        private String ticketNo;
        private String ticketState;
        private String workOrderKey;
        private String username;
        private String version;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CreateTicket implements HasTicketNo {
        @NotBlank(message = "Work order key cannot be empty!")
        private String workOrderKey;
        @Schema(description = "Ticket No.")
        @Null
        private String ticketNo;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SimpleTicketNo implements HasTicketNo {
        @NotNull(message = "必须指定工单票据ID")
        @Schema(description = "Ticket No.")
        private String ticketNo;
    }

    @Schema
    public static class DoNextStateOfTicket extends SimpleTicketNo {
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class SubmitTicket implements HasTicketNo {
        @NotNull(message = "必须指定工单票据ID")
        @Schema(description = "Ticket No.")
        private String ticketNo;
        @Schema(description = "申请说明")
        @NotBlank
        private String applyRemark;
        @Schema(description = "节点审批人信息")
        private List<NodeApprover> nodeApprovers;

        /**
         * key=nodeName, value=username
         */
        public Map<String, String> toApprovers() {
            return Optional.ofNullable(nodeApprovers)
                    .orElse(List.of())
                    .stream()
                    .collect(Collectors.toMap(NodeApprover::getNodeName, NodeApprover::getUsername));
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class NodeApprover {
        private String nodeName;
        private String username;
    }

    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class ApprovalTicket extends SimpleTicketNo {
        @Schema(description = "审批意见")
        private String approveRemark;
        private String approvalType;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddApplicationPermissionTicketEntry extends TicketEntry implements HasEntryDetail<UserPermissionBusinessParam.BusinessPermission>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 2888570476091613323L;
        private final String businessType = BusinessTypeEnum.APPLICATION.name();
        private UserPermissionBusinessParam.BusinessPermission detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddComputerPermissionTicketEntry extends TicketEntry implements HasEntryDetail<UserPermissionBusinessParam.BusinessPermission>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 1150566255112707316L;
        private final String businessType = BusinessTypeEnum.TAG_GROUP.name();
        private UserPermissionBusinessParam.BusinessPermission detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddServerAccountPermissionTicketEntry extends TicketEntry implements HasEntryDetail<UserPermissionBusinessParam.BusinessPermission>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 2888570476091613323L;
        private final String businessType = BusinessTypeEnum.SERVER_ACCOUNT.name();
        private UserPermissionBusinessParam.BusinessPermission detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddRevokeUserPermissionTicketEntry extends TicketEntry implements HasEntryDetail<UserVO.User>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -7031252644626013496L;
        private final String businessType = BusinessTypeEnum.USER.name();
        private UserVO.User detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddRevokeUserEdsAccountPermissionTicketEntry extends TicketEntry implements HasEntryDetail<EdsAssetVO.Asset>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 8822817329834870519L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private EdsAssetVO.Asset detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddGitLabProjectPermissionTicketEntry extends TicketEntry implements HasEntryDetail<GitLabPermissionModel.Permission>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 3955830707586605830L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private GitLabPermissionModel.Permission detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddGitLabGroupPermissionTicketEntry extends TicketEntry implements HasEntryDetail<GitLabPermissionModel.Permission>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -5401570571721716981L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private GitLabPermissionModel.Permission detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddApplicationElasticScalingTicketEntry extends TicketEntry implements HasEntryDetail<ApplicationReplicasModel.ApplicationConfigurationChange>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 3960401331035241929L;
        private final String businessType = BusinessTypeEnum.APPLICATION.name();
        private ApplicationReplicasModel.ApplicationConfigurationChange detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddApplicationDeploymentScaleTicketEntry extends TicketEntry implements HasEntryDetail<ApplicationDeploymentModel.DeploymentScale>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 8210926100811483441L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private ApplicationDeploymentModel.DeploymentScale detail;
    }


    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddAliyunDataWorksInstanceTicketEntry extends TicketEntry implements HasEntryDetail<ApplicationDeploymentModel.DeploymentScale>, BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 8210926100811483441L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private ApplicationDeploymentModel.DeploymentScale detail;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeleteTicketEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = 3693164673834109532L;
        @NotNull
        private Integer ticketId;
        @NotBlank
        private String businessType;
        @NotBlank
        private String entryKey;

        public WorkOrderTicketEntry totEntryUniqueKey() {
            return WorkOrderTicketEntry.builder()
                    .ticketId(ticketId)
                    .businessType(businessType)
                    .entryKey(entryKey)
                    .build();
        }
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TicketEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = -6950932321530742135L;
        private Integer ticketId;
        private String name;
        private String displayName;
        private Integer instanceId;
        private String businessType;
        private String subType;
        private Integer businessId;
        private String entryKey;
    }

}
