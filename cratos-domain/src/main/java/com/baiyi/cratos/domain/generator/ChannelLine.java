package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：channel_line
 * 表注释：渠道线路
 */
@Data
@Table(name = "channel_line")
public class ChannelLine implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 8707414256945487869L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 渠道ID
     */
    @Column(name = "channel_id")
    private Integer channelId;

    /**
     * 线路名称
     */
    private String name;

    /**
     * 线路类型
     */
    @Column(name = "line_type")
    private String lineType;

    /**
     * 源端
     */
    @Column(name = "source_endpoint")
    private String sourceEndpoint;

    /**
     * 监控URL
     */
    @Column(name = "monitor_url")
    private String monitorUrl;

    /**
     * 是否与渠道相连
     */
    @Column(name = "linked_channel")
    private Boolean linkedChannel;

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
     * 说明
     */
    private String comment;
}