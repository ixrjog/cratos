package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.generator.base.IValid;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "channel_network")
public class ChannelNetwork implements IValid {
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

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}