package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate implements HasValid, ToBusinessTarget, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -8880637546910076173L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 证书ID
     */
    @Column(name = "certificate_id")
    private String certificateId;

    /**
     * 名称
     */
    private String name;

    /**
     * 域名
     */
    @Column(name = "domain_name")
    private String domainName;

    /**
     * 证书类型
     */
    @Column(name = "certificate_type")
    private String certificateType;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 算法
     */
    @Column(name = "key_algorithm")
    private String keyAlgorithm;

    /**
     * 不早于
     */
    @Column(name = "not_before")
    private Date notBefore;

    /**
     * 不晚于
     */
    @Column(name = "not_after")
    private Date notAfter;

    @Column(name = "expired_time")
    private Date expiredTime;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}