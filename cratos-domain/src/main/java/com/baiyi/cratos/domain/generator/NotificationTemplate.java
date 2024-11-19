package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：notification_template
 * 表注释：通知模版
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "notification_template")
public class NotificationTemplate implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 2431865182955709322L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /**
     * 关键字
     */
    @Column(name = "notification_template_key")
    private String notificationTemplateKey;

    /**
     * 类型
     */
    @Column(name = "notification_template_type")
    private String notificationTemplateType;

    /**
     * 标题
     */
    private String title;

    /**
     * 消费者类型
     */
    private String consumer;

    private String lang;

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
     * 模板内容
     */
    private String content;

    /**
     * 描述
     */
    private String comment;
}