package com.baiyi.cratos.domain.param.http.work;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.*;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
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

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:34
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class CallbackApprovalTicket extends SimpleTicketNo {
        private String username;
        private String token;
        private String approvalType;

        public ApprovalTicket toApprovalTicket() {
            return ApprovalTicket.builder()
                    .approvalType(approvalType)
                    .approveRemark(approvalType.toUpperCase())
                    .ticketNo(getTicketNo())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryApplicationResourceDeploymentTicketEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = 7790611109888431617L;
        @NotBlank
        private String applicationName;
        @NotBlank
        private String namespace;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryAliyunKmsInstanceTicketEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = 9077732649157987094L;
        @NotNull
        private Integer instanceId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryAliyunKmsKeyTicketEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = -5889469691234285633L;
        @NotBlank
        private String kmsInstanceId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryLdapRolePermissionTicketEntry implements Serializable {
        @Serial
        private static final long serialVersionUID = 2103292539529876560L;
        @NotBlank
        private String group;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddApplicationPermissionTicketEntry extends TicketEntry<UserPermissionBusinessParam.BusinessPermission> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddComputerPermissionTicketEntry extends TicketEntry<UserPermissionBusinessParam.BusinessPermission> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddServerAccountPermissionTicketEntry extends TicketEntry<UserPermissionBusinessParam.BusinessPermission> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddCreateFrontEndApplicationTicketEntry extends TicketEntry<ApplicationModel.CreateFrontEndApplication> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -2362593723504211246L;
        private final String businessType = BusinessTypeEnum.APPLICATION.name();
        private ApplicationModel.CreateFrontEndApplication detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddRevokeUserPermissionTicketEntry extends TicketEntry<UserVO.User> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddRevokeUserEdsAccountPermissionTicketEntry extends TicketEntry<EdsAssetVO.Asset> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddGitLabProjectPermissionTicketEntry extends TicketEntry<GitLabPermissionModel.Permission> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddGitLabGroupPermissionTicketEntry extends TicketEntry<GitLabPermissionModel.Permission> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddApplicationElasticScalingTicketEntry extends TicketEntry<ApplicationReplicasModel.ApplicationConfigurationChange> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddApplicationDeploymentScaleTicketEntry extends TicketEntry<ApplicationDeploymentModel.DeploymentScale> implements BaseBusiness.HasBusinessType, Serializable {
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
    public static class AddAliyunDataWorksInstanceTicketEntry extends TicketEntry<AliyunDataWorksModel.AliyunAccount> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -4472407041713180447L;
        private final String businessType = BusinessTypeEnum.EDS_INSTANCE.name();
        private AliyunDataWorksModel.AliyunAccount detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddCreateAliyunKmsSecretTicketEntry extends TicketEntry<AliyunKmsModel.CreateSecret> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 1871415662334517238L;
        private final String businessType = BusinessTypeEnum.EDS_INSTANCE.name();
        private AliyunKmsModel.CreateSecret detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddUpdateAliyunKmsSecretTicketEntry extends TicketEntry<AliyunKmsModel.UpdateSecret> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 1871415662334517238L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private AliyunKmsModel.UpdateSecret detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddApplicationDeletePodTicketEntry extends TicketEntry<ApplicationVO.Application> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 3563509820230350553L;
        private final String businessType = BusinessTypeEnum.APPLICATION.name();
        private ApplicationVO.Application detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddDeploymentPodDeleteTicketEntry extends TicketEntry<ApplicationDeploymentModel.DeleteDeploymentPod> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -1244962359720286901L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private ApplicationDeploymentModel.DeleteDeploymentPod detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddDeploymentJvmSpecTicketEntry extends TicketEntry<ApplicationDeploymentModel.DeploymentJvmSpec> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -1244962359720286901L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private ApplicationDeploymentModel.DeploymentJvmSpec detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddApplicationRedeployTicketEntry extends TicketEntry<ApplicationVO.Application> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -8342340561313113489L;
        private final String businessType = BusinessTypeEnum.APPLICATION.name();
        private ApplicationVO.Application detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddDeploymentRedeployTicketEntry extends TicketEntry<ApplicationDeploymentModel.RedeployDeployment> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 8121444050004308889L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private ApplicationDeploymentModel.RedeployDeployment detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddAliyunRamPolicyPermissionTicketEntry extends TicketEntry<AliyunModel.AliyunPolicy> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 7964374299365455065L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private AliyunModel.AliyunPolicy detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddAwsIamPolicyPermissionTicketEntry extends TicketEntry<AwsModel.AwsPolicy> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -810336663173609420L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private AwsModel.AwsPolicy detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddCreateAliyunRamUserTicketEntry extends TicketEntry<AliyunModel.AliyunAccount> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 6363796292175321725L;
        private final String businessType = BusinessTypeEnum.EDS_INSTANCE.name();
        private AliyunModel.AliyunAccount detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddCreateAwsIamUserTicketEntry extends TicketEntry<AwsModel.AwsAccount> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 4654362333910726876L;
        private final String businessType = BusinessTypeEnum.EDS_INSTANCE.name();
        private AwsModel.AwsAccount detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddResetAliyunRamUserTicketEntry extends TicketEntry<AliyunModel.ResetAliyunAccount> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -5319582937582510299L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private AliyunModel.ResetAliyunAccount detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddResetAwsIamUserTicketEntry extends TicketEntry<AwsModel.ResetAwsAccount> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 6852356802450976743L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private AwsModel.ResetAwsAccount detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddCreateAwsTransferSftpUserTicketEntry extends TicketEntry<AwsTransferModel.SFTPUser> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 6852356802450976743L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private AwsTransferModel.SFTPUser detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddResetAlimailUserTicketEntry extends TicketEntry<EdsIdentityVO.MailAccount> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 847616175727551949L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private EdsIdentityVO.MailAccount detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddResetUserPasswordTicketEntry extends TicketEntry<UserVO.User> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 542391083392901537L;
        private final String businessType = BusinessTypeEnum.USER.name();
        private UserVO.User detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddLdapRolePermissionTicketEntry extends TicketEntry<LdapUserGroupModel.Role> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 160908446714673016L;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
        private LdapUserGroupModel.Role detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddCreateAliyunOnsTopicTicketEntry extends TicketEntry<AliyunOnsV5Model.Topic> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -432180644493765199L;
        private final String businessType = BusinessTypeEnum.EDS_INSTANCE.name();
        private AliyunOnsV5Model.Topic detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddCreateAliyunOnsConsumerGroupTicketEntry extends TicketEntry<AliyunOnsV5Model.ConsumerGroup> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = -1565391443339985819L;
        private final String businessType = BusinessTypeEnum.EDS_INSTANCE.name();
        private AliyunOnsV5Model.ConsumerGroup detail;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddRiskChangeTicketEntry extends TicketEntry<RiskChangeModel.RiskChangeApplication> implements BaseBusiness.HasBusinessType, Serializable {
        @Serial
        private static final long serialVersionUID = 9071773360612294130L;
        private final String businessType = BusinessTypeEnum.USER.name();
        private RiskChangeModel.RiskChangeApplication detail;
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
    public static class TicketEntry<T> implements HasEntryDetail<T>, Serializable {
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
        private String namespace;
        private T detail;
    }

}
