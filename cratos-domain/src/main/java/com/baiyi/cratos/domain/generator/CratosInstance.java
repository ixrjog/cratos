package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：cratos_instance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cratos_instance")
public class CratosInstance implements HasValid, HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 实例名
     */
    private String name;

    /**
     * 主机名
     */
    private String hostname;

    /**
     * 主机IP
     */
    @Column(name = "host_ip")
    private String hostIp;

    /**
     * 实例状态
     */
    private String status;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 实例版本信息
     */
    private String version;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String commit;

    private String license;
}