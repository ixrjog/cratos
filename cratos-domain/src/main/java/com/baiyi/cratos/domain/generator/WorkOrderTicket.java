package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.TicketWorkflow;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：work_order_ticket
 * 表注释：工单票据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_order_ticket")
public class WorkOrderTicket implements TicketWorkflow.HasWorkflow, HasIntegerPrimaryKey, HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Ticket No.
     */
    @Column(name = "ticket_no")
    private String ticketNo;

    /**
     * 工单ID
     */
    @Column(name = "work_order_id")
    private Integer workOrderId;

    /**
     * Created by
     */
    private String username;

    /**
     * 工单节点ID
     */
    @Column(name = "node_id")
    private Integer nodeId;

    /**
     * 状态
     */
    @Column(name = "ticket_state")
    private String ticketState;

    /**
     * 结果
     */
    @Column(name = "ticket_result")
    private String ticketResult;

    /**
     * 成功
     */
    private Boolean success;

    /**
     * 提交时间
     */
    @Column(name = "submitted_at")
    private Date submittedAt;

    /**
     * 完成
     */
    private Boolean completed;

    /**
     * 完成时间
     */
    @Column(name = "completed_at")
    private Date completedAt;

    /**
     * 自动处理
     */
    @Column(name = "auto_processing")
    private Boolean autoProcessing;

    /**
     * 处理时间
     */
    @Column(name = "process_at")
    private Date processAt;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 工作流快照
     */
    private String workflow;

    private String version;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 申请说明
     */
    @Column(name = "apply_remark")
    private String applyRemark;
}