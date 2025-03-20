package com.baiyi.cratos.domain.view.work;

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

    public interface HasTicketId {
        String getTicketId();
    }

    public interface HasTicket extends HasTicketId {
        void setTicket(Ticket ticket);
    }

    public interface HasTicketEntries extends HasTicketId {
        void setEntries(List<TicketEntry<?>> entries);
    }

    public interface HasTicketNodes extends HasTicketId {
        void setNodes(Map<String, TicketNode> nodes);
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
        private String ticketId;
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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Ticket implements Serializable {
        @Serial
        private static final long serialVersionUID = 9097666590091735099L;
        private Integer id;
        private String ticketId;
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
        private String comment;

        @Schema(description = "申请人")
        private UserVO.User applicant;
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
        private Integer businessType;
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
    }

}
