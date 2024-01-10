package com.baiyi.cratos.domain.generator;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * UUID
     */
    private String uuid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 显示名称
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 最后登录时间
     */
    @Column(name = "last_login")
    private Date lastLogin;

    /**
     * 手机
     */
    @Column(name = "mobile_phone")
    private String mobilePhone;

    /**
     * Two-Factor Authentication
     */
    private Integer otp;

    @Column(name = "created_by")
    private String createdBy;

    /**
     * 数据源
     */
    private String source;

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

    private String password;

    private String comment;
}