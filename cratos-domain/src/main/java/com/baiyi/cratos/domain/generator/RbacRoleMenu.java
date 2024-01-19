package com.baiyi.cratos.domain.generator;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "rbac_role_menu")
public class RbacRoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 子菜单ID
     */
    @Column(name = "submenu_id")
    private Integer submenuId;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}