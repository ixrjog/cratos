package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：eds_config
 * 表注释：EDS Config
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eds_config")
public class EdsConfig implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型
     */
    @Column(name = "eds_type")
    private String edsType;

    private String version;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 凭据ID
     */
    @Column(name = "credential_id")
    private Integer credentialId;

    /**
     * 数据实例ID
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 数据源地址
     */
    private String url;

    /**
     * 配置文件内容
     */
    @Column(name = "config_content")
    private String configContent;

    private String comment;
}