package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：acme_order
 * 表注释：ACME Order 表
 */
@Data
@Builder
@EncryptedDomain
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "acme_order")
public class AcmeOrder implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 5725728027097479520L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ACME 账户ID
     */
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "domain_id")
    private Integer domainId;

    /**
     * 证书ID(签发后关联)
     */
    @Column(name = "certificate_id")
    private Integer certificateId;

    /**
     * ACME Order URL
     */
    @Column(name = "order_url")
    private String orderUrl;

    /**
     * 状态: pending/ready/processing/valid/invalid
     */
    @Column(name = "order_status")
    private String orderStatus;

    /**
     * Order 过期时间
     */
    private Date expires;

    @Column(name = "dns_challenge_records")
    private String dnsChallengeRecords;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 域名列表(JSON数组)
     */
    private String domains;

    /**
     * 域名密钥对(PEM格式，建议数据库层加密)
     */
    @Column(name = "domain_key_pair")
    private String domainKeyPair;

    /**
     * 错误信息
     */
    @Column(name = "error_message")
    private String errorMessage;
}