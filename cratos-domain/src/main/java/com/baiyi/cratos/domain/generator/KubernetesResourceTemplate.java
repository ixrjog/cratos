package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
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
 * 表名：kubernetes_resource_template
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kubernetes_resource_template")
public class KubernetesResourceTemplate implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 3076658089268328663L;
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

    private Boolean locked;

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