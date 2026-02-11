package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：sre_event
 * 表注释：用户信息
 */
@Data
@Table(name = "sre_event")
public class SreEvent implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -5770131311894478832L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "event_no")
    private String eventNo;

    private String source;

    private String env;

    private String username;

    private String action;

    private String target;

    private String severity;

    @Column(name = "event_status")
    private String eventStatus;

    @Column(name = "event_time")
    private Date eventTime;

    private String module;

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

    private String description;

    private String tag;

    private String ext;

    @Column(name = "source_content")
    private String sourceContent;

    @Column(name = "target_content")
    private String targetContent;
}