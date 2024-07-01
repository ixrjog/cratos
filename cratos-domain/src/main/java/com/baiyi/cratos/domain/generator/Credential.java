package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.annotation.FieldEncrypt;
import com.baiyi.cratos.domain.generator.base.HasExpiredTime;
import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EncryptedDomain
public class Credential implements IValid, HasExpiredTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    /**
     * 凭据类型
     */
    @Column(name = "credential_type")
    private String credentialType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 指纹
     */
    private String fingerprint;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 凭据内容
     */
    @FieldEncrypt
    private String credential;

    /**
     * 凭据补充内容
     */
    @Column(name = "credential_2")
    @FieldEncrypt
    private String credential2;

    /**
     * 密码短语
     */
    @FieldEncrypt
    private String passphrase;

    @Column(name = "private_credential")
    private Boolean privateCredential;

    /**
     * 有效
     */
    private Boolean valid;

    private String comment;

    @Column(name = "expired_time")
    private Date expiredTime;
}