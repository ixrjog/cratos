package com.baiyi.cratos.domain.generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 表名：business_property
 * 表注释：Business Object Property
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "business_property")
public class BusinessProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Business Type
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * Business ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * Property Name
     */
    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * Property Value
     */
    @Column(name = "property_value")
    private String propertyValue;
}