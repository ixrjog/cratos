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
 * 表名：ssh_session_instance
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ssh_session_instance")
public class SshSessionInstance implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -6315359471434312859L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会话ID
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * 实例ID
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 会话复制实例ID
     */
    @Column(name = "duplicate_instance_id")
    private String duplicateInstanceId;

    /**
     * 实例会话类型
     */
    @Column(name = "instance_type")
    private String instanceType;

    /**
     * 登录账户
     */
    @Column(name = "login_user")
    private String loginUser;

    /**
     * IP
     */
    @Column(name = "dest_ip")
    private String destIp;

    /**
     * 输出文件大小
     */
    @Column(name = "output_size")
    private Long outputSize;

    /**
     * 是否关闭
     */
    @Column(name = "instance_closed")
    private Boolean instanceClosed;

    /**
     * 审计日志路径
     */
    @Column(name = "audit_path")
    private String auditPath;

    /**
     * 打开时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 关闭时间
     */
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}