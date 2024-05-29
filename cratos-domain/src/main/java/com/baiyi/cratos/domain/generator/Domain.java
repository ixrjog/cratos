package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import com.baiyi.cratos.domain.view.eds.ToBusinessTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：domain
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Domain implements IValid, ToBusinessTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 注册时间
     */
    @Column(name = "registration_time")
    private Date registrationTime;

    /**
     * 到期
     */
    private Date expiry;

    @Column(name = "domain_type")
    private String domainType;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}