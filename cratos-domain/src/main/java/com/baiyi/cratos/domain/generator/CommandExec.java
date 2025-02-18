package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：command_exec
 * 表注释：命令执行
 */
@Data
@Table(name = "command_exec")
public class CommandExec implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 5177272027658169256L;
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

    private String namespace;

    /**
     * 成功
     */
    private Boolean success;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 申请说明
     */
    @Column(name = "apply_remark")
    private String applyRemark;

    private String command;

    @Column(name = "exec_target_content")
    private String execTargetContent;

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