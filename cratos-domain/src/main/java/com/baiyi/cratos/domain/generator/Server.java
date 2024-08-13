package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：server
 */
@Data
public class Server implements IValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 显示名称
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * 服务器组ID
     */
    @Column(name = "server_group_id")
    private Integer serverGroupId;

    /**
     * 系统类型
     */
    @Column(name = "os_type")
    private String osType;

    /**
     * 环境类型
     */
    @Column(name = "env_name")
    private String envName;

    /**
     * 公网IP
     */
    @Column(name = "public_ip")
    private String publicIp;

    /**
     * 私网IP
     */
    @Column(name = "private_ip")
    private String privateIp;

    @Column(name = "remote_mgmt_ip")
    private String remoteMgmtIp;

    /**
     * 服务器类型
     */
    @Column(name = "server_type")
    private String serverType;

    /**
     * Serial Number
     */
    private String region;

    private String zone;

    /**
     * 监控状态
     */
    @Column(name = "monitor_status")
    private Integer monitorStatus;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 服务器状态
     */
    @Column(name = "server_status")
    private String serverStatus;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 说明
     */
    private String comment;
}