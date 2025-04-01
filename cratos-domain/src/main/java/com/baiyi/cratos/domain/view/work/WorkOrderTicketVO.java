package com.baiyi.cratos.domain.view.work;

import com.baiyi.cratos.domain.TicketWorkflow;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 16:43
 * &#064;Version 1.0
 */
public class WorkOrderTicketVO {

    public interface HasTicketNo {
        String getTicketNo();
    }

    public interface HasTicket extends HasTicketNo {
        void setTicket(Ticket ticket);
    }

    public interface HasTicketEntries extends HasTicketNo {
        void setEntries(List<TicketEntry<?>> entries);
    }

    public interface HasTicketNodes extends HasTicketNo {
        void setNodes(Map<String, TicketNode> nodes);

        void setCurrentNode(String currentNode);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TicketDetails implements HasTicket, HasTicketEntries, HasTicketNodes, Serializable {
        @Serial
        private static final long serialVersionUID = -3837715674384828343L;
        @Schema(description = "工单")
        private WorkOrderVO.WorkOrder workOrder;
        private String ticketNo;
        @Schema(description = "工单票据")
        private Ticket ticket;
        @Schema(description = "工单票据条目")
        private List<TicketEntry<?>> entries;

        private WorkflowModel.Workflow workflow;
        /**
         * key nodeName
         */
        @Schema(description = "节点详情")
        private Map<String, TicketNode> nodes;

        private String currentNode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Ticket implements TicketWorkflow.HasWorkflowData, Serializable {
        @Serial
        private static final long serialVersionUID = 9097666590091735099L;
        private Integer id;
        private String ticketNo;
        private Integer workOrderId;
        private String username;
        private Integer nodeId;
        private String ticketState;
        private String ticketResult;
        private Boolean success;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date submittedAt;
        private Boolean completed;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date completedAt;
        private Boolean autoProcessing;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date processAt;
        private String applyRemark;
        private Boolean valid;
        private String workflow;
        private WorkflowModel.Workflow workflowData;
        private String version;
        private String comment;

        @Schema(description = "申请人")
        private UserVO.User applicant;
        @Schema(description = "工单摘要")
        private TicketAbstract ticketAbstract;

        private WorkOrderVO.WorkOrder workOrder;

        @Schema(description = "申请人信息")
        private ApplicantInfo applicantInfo;
        @Schema(description = "审批人信息")
        private ApprovalInfo approvalInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TicketAbstract implements Serializable {
        @Serial
        private static final long serialVersionUID = 3119031268624002323L;
        public static final TicketAbstract NO_DATA = TicketAbstract.builder()
                .build();
        @Builder.Default
        private Integer entryCnt = 0;
        private String markdown;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TicketEntry<T> extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 7989247189791418176L;
        private Integer id;
        private Integer ticketId;
        private String name;
        private String displayName;
        private Integer instanceId;
        private String businessType;
        private String subType;
        private Integer businessId;
        private Boolean completed;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date completedAt;
        private String entryKey;
        private Boolean valid;
        private String namespace;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date executedAt;
        private Boolean success;
        private String comment;
        private String content;
        private T detail;
        private String result;

        public WorkOrderTicketEntry toTicketEntry() {
            return BeanCopierUtil.copyProperties(this, WorkOrderTicketEntry.class);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TicketNode extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 2685398949349696983L;
        private Integer id;
        private Integer ticketId;
        private String approvalType;
        private String nodeName;
        private String username;
        private Integer parentId;
        private String approvalStatus;
        private Date approvalAt;
        private Boolean approvalCompleted;
        private String comment;
        private String approveRemark;
        @Schema(description = "申请人信息")
        private ApplicantInfo applicantInfo;
        @Schema(description = "审批人信息")
        private ApprovalInfo approvalInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ApplicantInfo implements Serializable {
        public static final ApplicantInfo NOT_THE_APPLICANT = ApplicantInfo.builder()
                .isApplicant(false)
                .build();
        public static final ApplicantInfo THE_APPLICANT = ApplicantInfo.builder()
                .build();
        @Serial
        private static final long serialVersionUID = -5845232251566479793L;
        @Schema(description = "是当前申请人")
        @Builder.Default
        private boolean isApplicant = true;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ApprovalInfo implements Serializable {
        public static final ApprovalInfo NOT_THE_CURRENT_APPROVER = ApprovalInfo.builder()
                .isCurrentApprover(false)
                .approvalRequired(false)
                .build();
        @Serial
        private static final long serialVersionUID = -4541730852330178327L;
        @Schema(description = "是当前审批人")
        @Builder.Default
        private boolean isCurrentApprover = true;
        @Schema(description = "需要审批")
        private boolean approvalRequired;
    }

}
