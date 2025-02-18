package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：command_exec_approval
 * 表注释：命令执行审批
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "command_exec_approval")
public class CommandExecApproval implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 1590317008527949909L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "command_exec_id")
    private Integer commandExecId;

    /**
     * 审批节点类型
     */
    @Column(name = "approval_type")
    private String approvalType;

    /**
     * 审批人
     */
    private String username;

    /**
     * 审批状态
     */
    @Column(name = "approval_status")
    private String approvalStatus;

    /**
     * 完成时间
     */
    @Column(name = "approval_at")
    private Date approvalAt;

    /**
     * 审批完成
     */
    @Column(name = "approval_completed")
    private Boolean approvalCompleted;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 审批说明
     */
    @Column(name = "approve_remark")
    private String approveRemark;
}