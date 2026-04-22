package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：channel_business
 * 表注释：渠道业务
 */
@Data
@Table(name = "channel_business")
public class ChannelBusiness implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 411875678410212031L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 机构ID
     */
    @Column(name = "organization_id")
    private Integer organizationId;

    /**
     * 渠道ID
     */
    @Column(name = "channel_id")
    private Integer channelId;

    /**
     * 业务名称
     */
    @Column(name = "business_name")
    private String businessName;

    /**
     * 业务类型
     */
    private String type;

    /**
     * 业务方向(OUTBOUND/INBOUND)
     */
    @Column(name = "business_direction")
    private String businessDirection;

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