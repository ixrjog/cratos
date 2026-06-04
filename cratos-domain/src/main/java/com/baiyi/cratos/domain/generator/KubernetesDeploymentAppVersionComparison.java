package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：kubernetes_deployment_app_version_comparison
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 *           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
*/
@Data
@Table(name = "kubernetes_deployment_app_version_comparison")
public class KubernetesDeploymentAppVersionComparison implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -5305654560501043919L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "country_code")
    private String countryCode;

    private String namespace;

    @Column(name = "dc_kubernetes_name")
    private String dcKubernetesName;

    /**
     * 名称
     */
    @Column(name = "dc_kubernetes_instance_id")
    private Integer dcKubernetesInstanceId;

    @Column(name = "dr_kubernetes_name")
    private String drKubernetesName;

    @Column(name = "dr_kubernetes_instance_id")
    private Integer drKubernetesInstanceId;

    /**
     * 有效
     */
    private Boolean valid;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}