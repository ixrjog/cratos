package com.baiyi.cratos.domain.generator;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
 * 表名：rbac_role_menu
*/
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
     * 菜单ID
     */
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}