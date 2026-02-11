package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
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
 * 表名：acme_domain
 * 表注释：ACME Domain
 */
@Data
@Builder
@EncryptedDomain
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "acme_domain")
public class AcmeDomain implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 4190655398744828901L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 可见名称
     */
    private String name;

    @Column(name = "domain_id")
    private Integer domainId;

    /**
     * 域名
     */
    private String domain;

    private String domains;

    @Column(name = "zone_id")
    private String zoneId;

    /**
     * DNS 解析服务提供商
     */
    @Column(name = "dns_resolver_instance_id")
    private Integer dnsResolverInstanceId;

    @Column(name = "account_id")
    private Integer accountId;

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