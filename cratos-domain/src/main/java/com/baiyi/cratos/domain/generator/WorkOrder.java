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
 * 表名：work_order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_order")
public class WorkOrder implements HasIntegerPrimaryKey, HasValid, Serializable {
    @Serial
    private static final long serialVersionUID = 2061586323874022357L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 工单Key
     */
    @Column(name = "work_order_key")
    private String workOrderKey;

    /**
     * 工单组ID
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 状态
     */
    private String status;

    /**
     * 图标
     */
    private String icon;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 报表颜色
     */
    private String color;

    /**
     * 文档地址
     */
    private String docs;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 国际化
     */
    private String i18n;

    /**
     * 工作流配置
     */
    private String workflow;
}