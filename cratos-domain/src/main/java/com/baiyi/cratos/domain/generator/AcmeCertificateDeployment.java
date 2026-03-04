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
 * 表名：acme_certificate_deployment
 * 表注释：ACME证书部署表
*/
@Data
@Builder
@EncryptedDomain
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "acme_certificate_deployment")
public class AcmeCertificateDeployment implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 6110694288529680722L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 外部数据源实例ID
     */
    @Column(name = "eds_instance_id")
    private Integer edsInstanceId;

    /**
     * 外部数据源实例名称
     */
    @Column(name = "eds_instance_name")
    private String edsInstanceName;

    /**
     * 外部数据源证书ID
     */
    @Column(name = "eds_certificate_id")
    private String edsCertificateId;

    /**
     * 外部数据源证书名称
     */
    @Column(name = "eds_certificate_name")
    private String edsCertificateName;

    /**
     * ACME证书ID
     */
    @Column(name = "certificate_id")
    private Integer certificateId;

    private String domain;

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

    @Column(name = "deployment_details")
    private String deploymentDetails;
}