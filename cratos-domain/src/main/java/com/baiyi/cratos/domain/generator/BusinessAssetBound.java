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
 * 表名：business_asset_bind
 * 表注释：业务凭据
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "business_asset_bound")
public class BusinessAssetBound implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -6943360012509689935L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 资产ID
     */
    @Column(name = "asset_id")
    private Integer assetId;

    /**
     * 资产类型
     */
    @Column(name = "asset_type")
    private String assetType;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}