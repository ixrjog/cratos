package com.baiyi.cratos.domain.generator;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
public class Certificate {
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
    @Column(name = "is_active")
    private Boolean isActive;

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

    private String comment;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}