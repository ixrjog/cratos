package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：channel_extension
 * 表注释：渠道扩展
 */
@Data
@Table(name = "channel_extension")
public class ChannelExtension implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 8190676898489747388L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 渠道ID
     */
    @Column(name = "channel_id")
    private Integer channelId;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 角色
     */
    private String role;

    /**
     * 名称
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

    /**
     * 描述
     */
    private String comment;
}