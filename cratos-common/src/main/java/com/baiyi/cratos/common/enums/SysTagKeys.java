package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/19 11:35
 * &#064;Version 1.0
 */
@Getter
public enum SysTagKeys {

    EDS("EDS"),
    LEVEL("Level"),
    PRODUCT_LINE("ProductLine"),
    EXTERNAL_USER("ExternalUser"),
    BUSINESS("Business"),
    COMMAND_EXEC_APPROVER("CommandExecApprover"),
    COMMAND_EXEC("CommandExec"),
    USERNAME("Username"),
    CREATED_BY("CreatedBy"),
    SERVER_ACCOUNT("ServerAccount"),
    GROUP("Group"),
    USER_GROUP("UserGroup"),
    ENV("Env"),
    NAME("Name"),
    // 标识用户类型为非人员
    USER_TYPE("UserType"),
    EVENT("Event"),
    FRAMEWORK("Framework"),
    INGRESS_ORDER("IngressOrder"),
    KMS("KMS"),
    DATAWORKS("DataWorks"),
    DESCRIPTION("Description"),
    ENDPOINT("Endpoint"),
    CONFIG_MAP("ConfigMap"),
    ROCKET_MQ("RocketMQ"),
    FRONT_END("FrontEnd"),
    SSH_PROXY("SSHProxy"),
    COUNTRY_CODE("CountryCode"),
    FIN_LOSSES("FinLosses"),
    INSPECTION_NOTIFICATION("InspectionNotification"),
    ALERT_NOTIFICATION("AlertNotification"),
    DNS_RESOLVER("DNSResolver"),
    DOMAIN("Domain"),
    CERT_ABUSE("CertAbuse")
    ;

    private final String key;

    SysTagKeys(String key) {
        this.key = key;
    }

}
