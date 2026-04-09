package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：account_entity
 * 表注释：账户主体管理
 */
@Data
@Table(name = "account_entity")
public class AccountEntity implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -455357894092751597L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 账户名称
     */
    private String name;

    /**
     * 主体类型: COMPANY/INDIVIDUAL
     */
    @Column(name = "entity_type")
    private String entityType;

    /**
     * 注册名称(公司全称)
     */
    @Column(name = "registered_name")
    private String registeredName;

    /**
     * 注册国家
     */
    private String country;

    /**
     * 注册编号(营业执照号)
     */
    @Column(name = "registration_no")
    private String registrationNo;

    /**
     * 联系人
     */
    @Column(name = "contact_person")
    private String contactPerson;

    /**
     * 联系邮箱
     */
    @Column(name = "contact_email")
    private String contactEmail;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 是否有效
     */
    private Boolean valid;

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

    /**
     * 备注
     */
    private String comment;
}