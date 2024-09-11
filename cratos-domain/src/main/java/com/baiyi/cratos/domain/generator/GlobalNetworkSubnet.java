package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：global_network
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "global_network_subnet")
public class GlobalNetworkSubnet implements IValid, ToBusinessTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 主体名称
     */
    @Column(name = "main_name")
    private String mainName;

    /**
     * 主体类型
     */
    @Column(name = "main_type")
    private String mainType;

    @Column(name = "subnet_key")
    private String subnetKey;

    private String region;

    private String zone;

    /**
     * 主体ID
     */
    @Column(name = "main_id")
    private Integer mainId;

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