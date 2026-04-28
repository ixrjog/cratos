package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 表名：channel_business_node
 * 表注释：渠道业务线路关联
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channel_business_node")
public class ChannelBusinessNode implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 5649789814325336283L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 渠道业务ID
     */
    @Column(name = "channel_business_id")
    private Integer channelBusinessId;

    /**
     * 渠道线路ID
     */
    @Column(name = "channel_node_id")
    private Integer channelNodeId;

    /**
     * 有效
     */
    private Boolean valid;

    private Integer seq;

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
     * 说明
     */
    private String comment;
}
