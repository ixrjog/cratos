package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rbac_role")
public class RbacRole implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -4512528446577732727L;
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