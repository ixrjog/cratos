package com.baiyi.cratos.domain.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：business_asset_bind
 * 表注释：业务凭据
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "business_asset_bind")
public class BusinessAssetBind {
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

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}