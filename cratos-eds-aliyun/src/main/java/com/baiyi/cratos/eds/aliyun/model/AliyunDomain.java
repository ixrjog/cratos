package com.baiyi.cratos.eds.aliyun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/4/26 上午11:33
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AliyunDomain {
    private String ccompany;
    private String domainAuditStatus;
    private String domainGroupId;
    private String domainGroupName;
    private String domainName;
    private String domainStatus;
    private String domainType;
    private Integer expirationCurrDateDiff;
    private String expirationDate;
    private Long expirationDateLong;
    private String expirationDateStatus;
    private String instanceId;
    private Boolean premium;
    private String productId;
    private String registrantType;
    private String registrationDate;
    private Long registrationDateLong;
    private String remark;
    private String resourceGroupId;
}
