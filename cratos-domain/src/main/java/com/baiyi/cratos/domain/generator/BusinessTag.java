package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.BaseBusiness;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "business_tag")
public class BusinessTag implements BaseBusiness.IBusiness {
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

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}