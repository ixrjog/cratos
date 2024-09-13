package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 表名：robot
 * 表注释：机器人
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Robot implements IValid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 机器人名称
     */
    private String name;

    /**
     * 绑定用户
     */
    private String username;

    private String token;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 创建者
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 过期时间
     */
    @Column(name = "expired_time")
    private Date expiredTime;

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