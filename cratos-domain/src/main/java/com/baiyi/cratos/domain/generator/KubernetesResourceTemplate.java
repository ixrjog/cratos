package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：kubernetes_resource_template
*/
@Data
@Table(name = "kubernetes_resource_template")
public class KubernetesResourceTemplate implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 模板Key
     */
    @Column(name = "template_key")
    private String templateKey;

    /**
     * API Version
     */
    @Column(name = "api_version")
    private String apiVersion;

    private String custom;

    /**
     * 有效
     */
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

    /**
     * 描述
     */
    private String comment;
}