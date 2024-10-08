package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：eds_instance
 * 表注释：外部数据源实例
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eds_instance")
public class EdsInstance implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Instance Name
     */
    @Column(name = "instance_name")
    private String instanceName;

    /**
     * Instance Type
     */
    @Column(name = "eds_type")
    private String edsType;

    /**
     * 实例分类
     */
    private String kind;

    /**
     * 实例版本
     */
    private String version;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 数据源配置ID
     */
    @Column(name = "config_id")
    private Integer configId;

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
     * URL
     */
    private String url;

    private String comment;
}