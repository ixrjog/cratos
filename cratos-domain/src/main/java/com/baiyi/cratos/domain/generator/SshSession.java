package com.baiyi.cratos.domain.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：ssh_session
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ssh_session")
public class SshSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会话UUID
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 远端地址
     */
    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 会话状态
     */
    @Column(name = "session_status")
    private String sessionStatus;

    /**
     * 服务端主机名
     */
    @Column(name = "server_hostname")
    private String serverHostname;

    /**
     * 服务端地址
     */
    @Column(name = "server_addr")
    private String serverAddr;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "session_type")
    private String sessionType;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}