package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：traffic_layer_domain_record
 * 表注释：流量层记录
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "traffic_layer_domain_record")
public class TrafficLayerDomainRecord implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 域名ID
     */
    @Column(name = "domain_id")
    private Integer domainId;

    /**
     * 环境名称
     */
    @Column(name = "env_name")
    private String envName;

    /**
     * 记录名
     */
    @Column(name = "record_name")
    private String recordName;

    /**
     * 流量路由至
     */
    @Column(name = "route_traffic_to")
    private String routeTrafficTo;

    /**
     * 源站点
     */
    @Column(name = "origin_server")
    private String originServer;

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