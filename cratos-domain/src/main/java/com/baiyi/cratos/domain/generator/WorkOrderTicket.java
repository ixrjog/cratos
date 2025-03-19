package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
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
public class WorkOrderTicket implements HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 字符串ID
     */
    @Column(name = "ticket_id")
    private String ticketId;

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
     * 当前审批结果
     */
    @Column(name = "current_approval_result")
    private String currentApprovalResult;

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
     * 自动执行
     */
    @Column(name = "auto_execute")
    private Boolean autoExecute;

    /**
     * 执行时间
     */
    @Column(name = "executed_at")
    private Date executedAt;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 申请说明
     */
    @Column(name = "apply_remark")
    private String applyRemark;
}