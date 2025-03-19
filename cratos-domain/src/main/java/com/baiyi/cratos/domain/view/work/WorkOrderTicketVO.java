package com.baiyi.cratos.domain.view.work;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 16:43
 * &#064;Version 1.0
 */
public class WorkOrderTicketVO {

    public interface HasTicket {
        void setTicket(Ticket ticket);

        String getTicketId();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TicketDetails implements HasTicket, Serializable {
        @Serial
        private static final long serialVersionUID = -3837715674384828343L;
        @Schema(description = "工单")
        private WorkOrderVO.WorkOrder workOrder;
        private String ticketId;
        @Schema(description = "工单票据")
        private Ticket ticket;
        @Schema(description = "工单票据条目")
        private List<Entry<?>> entries;
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
        private String currentApprovalResult;
        private Boolean success;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date submittedAt;
        private Boolean completed;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date completedAt;
        private Boolean autoExecute;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date executedAt;
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
    public static class Entry<T> extends BaseVO implements Serializable {
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
    }

}
