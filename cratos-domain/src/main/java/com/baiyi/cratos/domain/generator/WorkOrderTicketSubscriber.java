package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：work_order_ticket_subscriber
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_order_ticket_subscriber")
public class WorkOrderTicketSubscriber implements HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单票据ID
     */
    @Column(name = "ticket_id")
    private Integer ticketId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 订阅者类型
     */
    @Column(name = "subscribe_status")
    private String subscribeStatus;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 免登录审批令牌
     */
    private String token;

    /**
     * 说明
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}