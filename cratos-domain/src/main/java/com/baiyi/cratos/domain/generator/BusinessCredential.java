package com.baiyi.cratos.domain.generator;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "business_credential")
public class BusinessCredential {
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
     * 凭据ID
     */
    @Column(name = "credential_id")
    private Integer credentialId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}