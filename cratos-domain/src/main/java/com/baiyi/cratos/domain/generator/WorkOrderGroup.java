package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：work_order_group
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_order_group")
public class WorkOrderGroup implements HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单组名称
     */
    private String name;

    /**
     * 国际化
     */
    private String i18n;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 工单组类型
     */
    @Column(name = "group_type")
    private String groupType;

    /**
     * 图标
     */
    private String icon;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}