package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：application_resource
 * 表注释：应用资源
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_resource")
public class ApplicationResource implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 8847037734490068571L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "application_name")
    private String applicationName;

    @Column(name = "instance_name")
    private String instanceName;

    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "business_id")
    private Integer businessId;

    @Column(name = "business_type")
    private String businessType;

    private String namespace;

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

    private String comment;
}