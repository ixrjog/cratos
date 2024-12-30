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
 * 表名：application_resource_baseline_member
 * 表注释：Application Baseline Member
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_resource_baseline_member")
public class ApplicationResourceBaselineMember implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = -2352721901116152865L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "baseline_id")
    private Integer baselineId;

    @Column(name = "baseline_type")
    private String baselineType;

    @Column(name = "application_name")
    private String applicationName;

    private String name;

    private String namespace;

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

    @Column(name = "baseline_content")
    private String baselineContent;

    private String content;

    private String comment;
}