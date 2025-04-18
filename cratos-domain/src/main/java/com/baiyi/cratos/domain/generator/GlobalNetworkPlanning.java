package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：global_network_planning
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 */
@Data
@Table(name = "global_network_planning")
public class GlobalNetworkPlanning implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 6001597082819520549L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "network_id")
    private Integer networkId;

    /**
     * 名称
     */
    private String name;

    @Column(name = "cidr_block")
    private String cidrBlock;

    @Column(name = "resource_total")
    private Integer resourceTotal;

    /**
     * 有效
     */
    private Boolean valid;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}