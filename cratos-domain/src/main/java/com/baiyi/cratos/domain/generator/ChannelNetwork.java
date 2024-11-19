package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import com.baiyi.cratos.domain.generator.base.HasValid;
import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "channel_network")
public class ChannelNetwork implements HasValid, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 8589909374167078031L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "channel_key")
    private String channelKey;

    /**
     * 有效
     */
    private Boolean valid;

    /**
     * 渠道状态
     */
    @Column(name = "channel_status")
    private String channelStatus;

    /**
     * 可用状态 HA 2UP 1UP DOWN
     */
    @Column(name = "available_status")
    private String availableStatus;

    private String comment;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}