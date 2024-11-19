package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：risk_event
 * 表注释：风险事件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "risk_event")
public class RiskEvent implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 815139847154790002L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件时间
     */
    @Column(name = "event_time")
    private Date eventTime;

    /**
     * 状态
     */
    private String states;

    /**
     * 年
     */
    private String year;

    /**
     * 季
     */
    private String quarter;

    /**
     * 周
     */
    private Integer weeks;

    /**
     * 有效
     */
    private Boolean valid;

    private String color;

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

    private String comment;
}