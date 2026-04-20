package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：channel
 * 表注释：渠道信息
 */
@Data
public class Channel implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 8139594004777589953L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 监控URL
     */
    @Column(name = "monitor_url")
    private String monitorUrl;

    /**
     * 级别(优先级)
     */
    private Integer priority;

    /**
     * 国家
     */
    private String country;

    /**
     * 可用状态
     */
    @Column(name = "available_status")
    private String availableStatus;

    /**
     * 建设阶段
     */
    @Column(name = "construction_phase")
    private String constructionPhase;

    /**
     * 有效
     */
    private Boolean valid;

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
     * 网络信息(Markdown)
     */
    @Column(name = "network_info")
    private String networkInfo;

    /**
     * 描述
     */
    private String comment;
}