package com.baiyi.cratos.domain.generator;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasIntegerPrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_tag")
public class BusinessTag implements BaseBusiness.HasBusiness, HasIntegerPrimaryKey, Serializable {
    @Serial
    private static final long serialVersionUID = 8112239664667850417L;
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