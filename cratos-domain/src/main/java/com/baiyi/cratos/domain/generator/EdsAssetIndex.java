package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：eds_asset_index
 * 表注释：数据源实例资产
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eds_asset_index")
public class EdsAssetIndex implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -4849818071687150451L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据源实例ID
     */
    @Column(name = "instance_id")
    private Integer instanceId;

    /**
     * 资产ID
     */
    @Column(name = "asset_id")
    private Integer assetId;

    /**
     * 索引名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

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

    private String comment;
}