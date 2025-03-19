package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：work_order_ticket_node
 * 表注释：工单票据节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_order_ticket_node")
public class WorkOrderTicketNode implements HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单票据ID
     */
    @Column(name = "ticket_id")
    private Integer ticketId;

    /**
     * 审批类型
     */
    @Column(name = "approval_type")
    private String approvalType;

    /**
     * 节点名称
     */
    @Column(name = "node_name")
    private String nodeName;

    /**
     * 审批人
     */
    private String username;

    /**
     * 父流程ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 审批状态
     */
    @Column(name = "approval_status")
    private String approvalStatus;

    @Column(name = "approval_at")
    private Date approvalAt;

    @Column(name = "approval_completed")
    private Boolean approvalCompleted;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "approve_remark")
    private String approveRemark;
}