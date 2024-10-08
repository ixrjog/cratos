package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：asset_maturity
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 *           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
*/
@Data
@Table(name = "asset_maturity")
public class AssetMaturity implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "subscription_time")
    private Date subscriptionTime;

    /**
     * 到期
     */
    private Date expiry;

    /**
     * 有效
     */
    private Boolean valid;

    @Column(name = "auto_renewal")
    private Boolean autoRenewal;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}