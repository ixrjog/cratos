package com.baiyi.cratos.domain.generator;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    /**
     * 凭据类型
     */
    @Column(name = "credential_type")
    private String credentialType;

    /**
     * 用户名
     */
    private String username;

    /**
     * 指纹
     */
    private String fingerprint;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 凭据内容
     */
    private String credential;

    /**
     * 凭据补充内容
     */
    @Column(name = "credential_2")
    private String credential2;

    /**
     * 密码短语
     */
    private String passphrase;

    private String comment;
}