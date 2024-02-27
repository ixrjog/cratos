package com.baiyi.cratos.domain.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：eds_asset
 * 表注释：数据源实例资产
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eds_asset")
public class EdsAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产父关系
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 数据源实例ID
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    /**
     * 资产名称
     */
    private String name;

    /**
     * 数据源实例中资产的唯一ID
     */
    @Column(name = "asset_id")
    private String assetId;

    /**
     * 资产关键字
     */
    @Column(name = "asset_key")
    private String assetKey;

    /**
     * 资产类型
     */
    @Column(name = "asset_type")
    private String assetType;

    /**
     * 种类
     */
    private String kind;

    /**
     * 资产版本
     */
    private String version;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 地区ID
     */
    private String region;

    /**
     * 区域
     */
    private String zone;

    /**
     * 资产状态
     */
    @Column(name = "asset_status")
    private String assetStatus;

    /**
     * 资产创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 资产过期时间
     */
    @Column(name = "expired_time")
    private Date expiredTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 原始模型
     */
    @Column(name = "original_model")
    private String originalModel;

    private String description;
}