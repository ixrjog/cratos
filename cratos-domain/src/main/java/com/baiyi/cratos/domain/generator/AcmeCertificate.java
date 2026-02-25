package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.annotation.FieldEncrypt;
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
 * 表名：acme_certificate
 * 表注释：ACME证书表
*/
@Data
@Builder
@EncryptedDomain
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "acme_certificate")
public class AcmeCertificate  implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -6235273906282746657L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ACME账户ID
     */
    @Column(name = "account_id")
    private Integer accountId;

    /**
     * 域名ID
     */
    @Column(name = "domain_id")
    private Integer domainId;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Integer orderId;

    /**
     * 域名列表(逗号分隔)
     */
    private String domains;

    /**
     * 证书生效时间
     */
    @Column(name = "not_before")
    private Date notBefore;

    /**
     * 证书过期时间
     */
    @Column(name = "not_after")
    private Date notAfter;

    /**
     * 证书序列号
     */
    @Column(name = "serial_number")
    private String serialNumber;

    /**
     * 签发者
     */
    private String issuer;

    /**
     * 是否有效
     */
    private Boolean valid;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 证书内容(PEM格式)
     */
    private String certificate;

    /**
     * 完整证书链
     */
    @Column(name = "certificate_chain")
    private String certificateChain;

    /**
     * 私钥(加密存储)
     */
    @Column(name = "private_key")
    @FieldEncrypt
    private String privateKey;
}