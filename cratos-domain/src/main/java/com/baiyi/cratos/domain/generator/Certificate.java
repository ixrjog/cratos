package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Certificate implements IValid {
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