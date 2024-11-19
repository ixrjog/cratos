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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rbac_resource")
public class RbacResource implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 847353882994175054L;
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