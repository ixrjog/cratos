package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Table(name = "rbac_resource")
public class RbacResource implements IValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源组ID
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 资源名称
     */
    @Column(name = "resource_name")
    private String resourceName;

    private String comment;

    /**
     * 是否鉴权
     */
    private Boolean valid;

    /**
     * 用户界面端点
     */
    @Column(name = "ui_point")
    private Boolean uiPoint;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}