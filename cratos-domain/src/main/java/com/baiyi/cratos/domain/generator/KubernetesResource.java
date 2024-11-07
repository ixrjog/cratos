package com.baiyi.cratos.domain.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：kubernetes_resource
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kubernetes_resource")
public class KubernetesResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "member_id")
    private Integer memberId;

    private String name;

    /**
     * 命名空间
     */
    private String namespace;

    private String kind;

    @Column(name = "eds_instance_id")
    private Integer edsInstanceId;

    @Column(name = "asset_id")
    private Integer assetId;

    @Column(name = "created_by")
    private String createdBy;

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

    /**
     * 自定义
     */
    private String custom;

    /**
     * 描述
     */
    private String comment;
}