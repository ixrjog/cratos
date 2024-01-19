package com.baiyi.cratos.domain.generator;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "rbac_group")
public class RbacGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name")
    private String groupName;

    /**
     * 基本路径
     */
    private String base;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}