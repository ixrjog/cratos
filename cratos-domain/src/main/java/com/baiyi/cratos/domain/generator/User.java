package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.generator.base.IValid;
import com.baiyi.cratos.domain.view.eds.IToBusinessTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EncryptedDomain
public class User implements IValid, IToBusinessTarget {
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
    private Boolean valid;

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

    private String lang;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "expired_time")
    private Date expiredTime;

    private String password;

    private String comment;
}