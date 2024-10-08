package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：menu
*/
@Data
public class Menu implements HasValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String icon;

    private String link;

    /**
     * 顺序
     */
    private Integer seq;

    /**
     * Parent ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    private Boolean valid;

    /**
     * 类型
     */
    @Column(name = "menu_type")
    private String menuType;

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
}