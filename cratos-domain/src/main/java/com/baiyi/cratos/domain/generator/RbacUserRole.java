package com.baiyi.cratos.domain.generator;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "rbac_user_role")
public class RbacUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}