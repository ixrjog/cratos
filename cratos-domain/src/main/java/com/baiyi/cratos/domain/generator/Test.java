package com.baiyi.cratos.domain.generator;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：test
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 *           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
*/
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 环境名称
     */
    @Column(name = "env_name")
    private String envName;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 环境色
     */
    private String color;

    /**
     * 终端提示色
     */
    @Column(name = "prompt_color")
    private Integer promptColor;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * 有效
     */
    private Boolean valid;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;
}