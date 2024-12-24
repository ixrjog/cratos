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
 * 表名：application_actuator
 * 表注释：Application Actuator
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_actuator")
public class ApplicationActuator implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 6892522870016628536L;
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

    private String framework;

    private Boolean standard;

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

    @Column(name = "liveness_probe")
    private String livenessProbe;

    @Column(name = "readiness_probe")
    private String readinessProbe;

    @Column(name = "startup_probe")
    private String startupProbe;

    private String lifecycle;

    private String comment;
}