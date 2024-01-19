package com.baiyi.cratos.domain.generator;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "rbac_role")
public class RbacRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 访问级别
     */
    @Column(name = "access_level")
    private Integer accessLevel;

    /**
     * 工单中可见
     */
    @Column(name = "work_order_visible")
    private Boolean workOrderVisible;

    /**
     * 角色描述
     */
    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}