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
 * 表名：menu_title
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_title")
public class MenuTitle implements HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 5303395822740557816L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id")
    private Integer menuId;

    /**
     * 标题
     */
    private String title;

    /**
     * 菜单标题国际化
     */
    private String lang;

    /**
     * 首选
     */
    private Boolean preference;

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