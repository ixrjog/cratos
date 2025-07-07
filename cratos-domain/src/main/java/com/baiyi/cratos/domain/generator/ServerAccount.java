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

/**
 * 表名：server_account
 * 表注释：服务器账户
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "server_account")
public class ServerAccount implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -6329257651888231227L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Account Name
     */
    private String name;

    /**
     * Login Username
     */
    private String username;

    /**
     * 凭据ID
     */
    @Column(name = "credential_id")
    private Integer credentialId;

    /**
     * 特权
     */
    private Boolean sudo;

    /**
     * 连接协议
     */
    private String protocol;

    /**
     * 有效
     */
    private Boolean valid;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 说明
     */
    private String comment;
}