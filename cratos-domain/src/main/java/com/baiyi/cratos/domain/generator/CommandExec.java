package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：command_exec
 * 表注释：命令执行
 */
@Data
@Table(name = "command_exec")
public class CommandExec implements HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 申请人
     */
    private String username;

    /**
     * 自动执行
     */
    @Column(name = "auto_exec")
    private Boolean autoExec;

    /**
     * 由谁审批
     */
    @Column(name = "approved_by")
    private String approvedBy;

    /**
     * 抄送
     */
    @Column(name = "cc_to")
    private String ccTo;

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
     * 成功
     */
    private Boolean success;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 申请说明
     */
    @Column(name = "apply_remark")
    private String applyRemark;

    private String command;

    /**
     * 审批说明
     */
    @Column(name = "approve_remark")
    private String approveRemark;

    /**
     * 标准输出
     */
    @Column(name = "out_msg")
    private String outMsg;

    /**
     * 错误信息
     */
    @Column(name = "error_msg")
    private String errorMsg;
}