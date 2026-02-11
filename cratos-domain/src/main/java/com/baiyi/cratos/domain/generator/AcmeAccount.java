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
 * 表名：acme_account
 * 表注释：ACME 账户表
*/
@Data
@Builder
@EncryptedDomain
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "acme_account")
public class AcmeAccount implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 6378906339824603485L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /**
     * 账户邮箱
     */
    private String email;

    /**
     * CA 供应商: letsencrypt/zerossl/google/buypass
     */
    @Column(name = "acme_provider")
    private String acmeProvider;

    /**
     * ACME 账户 URL
     */
    @Column(name = "account_url")
    private String accountUrl;

    /**
     * ACME 服务器 URL
     */
    @Column(name = "acme_server")
    private String acmeServer;

    @Column(name = "created_by")
    private String createdBy;

    /**
     * 是否有效: 1-有效 0-无效
     */
    private Boolean valid;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 账户密钥对(加密存储)
     */
    @Column(name = "account_key_pair")
    @FieldEncrypt
    private String accountKeyPair;
}