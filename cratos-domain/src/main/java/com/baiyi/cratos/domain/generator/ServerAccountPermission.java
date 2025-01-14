package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：server_account_permission
 * 表注释：服务器-账户 授权表
*/
@Data
@Table(name = "server_account_permission")
public class ServerAccountPermission implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 1860798551077637387L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Server ID
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * Account ID
     */
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}