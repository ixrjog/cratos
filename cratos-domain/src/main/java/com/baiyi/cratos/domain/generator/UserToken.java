package com.baiyi.cratos.domain.generator;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Table(name = "user_token")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户登录名
     */
    private String username;

    /**
     * 登录唯一标识
     */
    private String token;

    /**
     * 是否有效
     */
    private Boolean valid;

    private String comment;

    @Column(name = "expired_time")
    private Date expiredTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}