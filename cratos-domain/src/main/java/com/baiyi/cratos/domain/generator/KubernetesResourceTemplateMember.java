package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：kubernetes_resource_template_member
 */
@Data
@Table(name = "kubernetes_resource_template_member")
public class KubernetesResourceTemplateMember implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "template_id")
    private Integer templateId;

    /**
     * 命名空间
     */
    private String namespace;

    private String kind;

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
     * 模板内容
     */
    private String content;

    /**
     * 自定义
     */
    private String custom;

    /**
     * 描述
     */
    private String comment;
}