package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：traffic_record_target
 * 表注释：流量切换
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "traffic_record_target")
public class TrafficRecordTarget implements HasValid, HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "traffic_route_id")
    private Integer trafficRouteId;

    /**
     * 解析类型
     */
    @Column(name = "record_type")
    private String recordType;

    /**
     * Resource Record
     */
    @Column(name = "resource_record")
    private String resourceRecord;

    /**
     * 记录值
     */
    @Column(name = "record_value")
    private String recordValue;

    /**
     * CDN|WAF|LB
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 是否源站
     */
    private Boolean origin;

    /**
     * TTL
     */
    private Long ttl;

    /**
     * 权重
     */
    private Integer weight;

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

    private String comment;
}