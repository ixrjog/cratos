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
 * 表名：work_order_ticket_entry
 * 表注释：工单票据条目
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_order_ticket_entry")
public class WorkOrderTicketEntry implements HasValid, HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单ID
     */
    @Column(name = "ticket_id")
    private Integer ticketId;

    /**
     * 名称
     */
    private String name;

    /**
     * 条目显示名
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * 数据源实例ID
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 子类型
     */
    @Column(name = "sub_type")
    private String subType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 完成
     */
    private Boolean completed;

    @Column(name = "completed_at")
    private Date completedAt;

    /**
     * 条目Key
     */
    @Column(name = "entry_key")
    private String entryKey;

    private Boolean valid;

    /**
     * 命名空间
     */
    private String namespace;

    @Column(name = "executed_at")
    private Date executedAt;

    private Boolean success;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 处理结果
     */
    private String result;
}