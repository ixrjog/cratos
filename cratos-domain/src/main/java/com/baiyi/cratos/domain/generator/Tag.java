package com.baiyi.cratos.domain.generator;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 标签类型
     */
    @Column(name = "tag_type")
    private Integer tagType;

    /**
     * 标签Key
     */
    @Column(name = "tag_key")
    private String tagKey;

    /**
     * 标签值
     */
    @Column(name = "tag_value")
    private String tagValue;

    /**
     * 颜色
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
    @Column(name = "is_active")
    private Boolean isActive;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}