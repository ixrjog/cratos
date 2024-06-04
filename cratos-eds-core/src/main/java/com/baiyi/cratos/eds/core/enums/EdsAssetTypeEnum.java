package com.baiyi.cratos.eds.core.enums;

import lombok.Getter;

/**
 * &#064;Author baiyi
 * &#064;Date 2024/2/26 13:48
 * &#064;Version 1.0
 */
@Getter
public enum EdsAssetTypeEnum {

    ALIYUN_CERT("Certificate"),
    ALIYUN_ALB("ALB"),
    ALIYUN_ECS("ECS"),
    ALIYUN_DOMAIN("Domain"),
    ALIYUN_RAM_USER("RAM User"),
    ALIYUN_RAM_POLICY("RAM Policy"),
    AWS_CERT("Certificate"),
    AWS_STS_VPN("Site-to-Site VPN"),
    AWS_ELB("ELB"),
    AWS_EC2("EC2"),
    AWS_EBS("EBS"),
    AWS_DOMAIN("Domain"),
    AWS_SQS_QUEUE("SQS Queue"),
    AWS_SNS_TOPIC("SNS Topic"),
    AWS_SNS_SUBSCRIPTION("SNS Subscription"),
    HUAWEICLOUD_ECS("ECS"),
    HUAWEICLOUD_IAM_USER("IAM User"),
    CLOUDFLARE_CERT("Certificate"),
    KUBERNETES_NAMESPACE("Namespace"),
    KUBERNETES_DEPLOYMENT("Deployment"),
    KUBERNETES_INGRESS("Ingress"),
    KUBERNETES_SERVICE("Service"),
    KUBERNETES_NODE("Node"),
    KUBERNETES_VIRTUAL_SERVICE("VirtualService"),
    KUBERNETES_DESTINATION_RULE("DestinationRule"),
    LDAP_PERSON("Person"),
    LDAP_GROUP("Group"),

    DINGTALK_USER("User"),
    DINGTALK_DEPARTMENT("Department"),
    DINGTALK_ROBOT_MSG("RobotMsg"),

    GITLAB_PROJECT("Project"),
    GITLAB_GROUP("Group"),
    GITLAB_USER("User"),
    GITLAB_SSHKEY("SSH Key"),
    GANDI_DOMAIN("Domain"),;

    private final String displayName;

    EdsAssetTypeEnum(String displayName) {
        this.displayName = displayName;
    }

}
