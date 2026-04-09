package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：datacenter_network_allocation
 * 表注释：数据中心网络分配
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "datacenter_network_allocation")
public class DatacenterNetworkAllocation implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 4761999162861552542L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联datacenter_network.id
     */
    @Column(name = "network_id")
    private Integer networkId;

    /**
     * 分配名称
     */
    private String name;

    private String region;

    /**
     * CIDR 如 10.20.0.0/24
     */
    private String cidr;

    /**
     * 起始IP(整数)
     */
    @Column(name = "ip_start")
    private Long ipStart;

    /**
     * 结束IP(整数)
     */
    @Column(name = "ip_end")
    private Long ipEnd;

    /**
     * 分配类型: SUBNET/VPC/VLAN/RESERVED
     */
    @Column(name = "allocation_type")
    private String allocationType;

    /**
     * 允许CIDR冲突
     */
    @Column(name = "allow_overlap")
    private Boolean allowOverlap;

    /**
     * 是否有效
     */
    private Boolean valid;

    private String nat;

    /**
     * 备注
     */
    private String comment;

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
}