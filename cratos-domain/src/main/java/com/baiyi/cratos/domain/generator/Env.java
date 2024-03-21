package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：env
 * 表注释：`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 *           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
*/
@Data
public class Env implements IValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 环境名称
     */
    @Column(name = "env_name")
    private String envName;

    /**
     * 颜色
     */
    private String color;

    /**
     * 终端提示色
     */
    @Column(name = "prompt_color")
    private String promptColor;

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

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}