package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：application
 * 表注释：聚合应用
 */
@Data
public class Application implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

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

    private String config;

    /**
     * 描述
     */
    private String comment;
}