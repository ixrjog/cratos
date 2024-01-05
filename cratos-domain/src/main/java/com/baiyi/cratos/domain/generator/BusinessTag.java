package com.baiyi.cratos.domain.generator;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "business_tag")
public class BusinessTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 标签ID
     */
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "tag_value")
    private String tagValue;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}