package com.baiyi.cratos.domain.generator;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

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

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}