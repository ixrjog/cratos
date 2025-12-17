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
 * 表名：traffic_route
 * 表注释：流量切换
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "traffic_route")
public class TrafficRoute implements HasValid, HasIntegerPrimaryKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "domain_id")
    private Integer domainId;

    @Column(name = "domain_record_id")
    private Integer domainRecordId;

    /**
     * 域名
     */
    private String domain;

    @Column(name = "domain_record")
    private String domainRecord;

    /**
     * 名称
     */
    private String name;

    /**
     * DNS 解析服务提供商
     */
    @Column(name = "dns_resolver_instance_id")
    private Integer dnsResolverInstanceId;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "zone_id")
    private String zoneId;

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