package com.baiyi.cratos.domain.param.http.work;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
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

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SimpleTicketNo implements HasTicketNo {
        private String ticketNo;
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
        private List<NodeApprover> nodeApprovals;

        /**
         * key=nodeName, value=username
         */
        public Map<String, String> toApprover() {
            return nodeApprovals.stream()
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

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema
    public static class ApprovalTicket implements HasTicketNo {
        @NotNull(message = "必须指定工单票据ID")
        @Schema(description = "Ticket No.")
        private String ticketNo;
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
