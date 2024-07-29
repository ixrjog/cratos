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
    ALIYUN_OSS_BUCKET("OSS Bucket"),
    ALIYUN_ARMS_TRACE_APPS("ARMS Trace Apps"),
    ALIYUN_DDS_MONGO_INSTANCE("MongoDB"),
    ALIYUN_ONS_V5_INSTANCE("ONS(5.0) Instance"),
    ALIYUN_ONS_V5_TOPIC("ONS(5.0) Topic"),
    ALIYUN_ONS_V5_CONSUMER_GROUP("ONS(5.0) ConsumerGroup"),
    ALIYUN_ONS_V5_CONSUMER_GROUP_SUBSCRIPTION("ONS(5.0) Subscription"),
    ALIYUN_ACR_NAMESPACE("ACR Namespace"),
    ALIYUN_ACR_INSTANCE("ACR Instance"),
    ALIYUN_ACR_REPOSITORY("ACR Repository"),

    AWS_CERT("Certificate"),
    AWS_STS_VPN("Site-to-Site VPN"),
    AWS_ELB("ELB"),
    AWS_EC2("EC2"),
    AWS_EBS("EBS"),
    AWS_DOMAIN("Domain"),
    AWS_SQS_QUEUE("SQS Queue"),
    AWS_SNS_TOPIC("SNS Topic"),
    AWS_SNS_SUBSCRIPTION("SNS Subscription"),
    AWS_TRANSFER_SERVER("Transfer Server"),
    AWS_CLOUDFRONT_DISTRIBUTION("CloudFront Distribution"),
    AWS_S3_BUCKET("S3 Bucket"),
    AWS_ECR_REPOSITORY("ECR Repository"),

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
    KUBERNETES_ALIBABACLOUD_AUTOSCALER("Alibabacloud Autoscaler"),

    LDAP_PERSON("Person"),
    LDAP_GROUP("Group"),

    DINGTALK_USER("User"),
    DINGTALK_DEPARTMENT("Department"),
    DINGTALK_ROBOT_MSG("RobotMsg"),

    GITLAB_PROJECT("Project"),
    GITLAB_GROUP("Group"),
    GITLAB_USER("User"),
    GITLAB_SSHKEY("SSH Key"),
    GANDI_DOMAIN("Domain"),

    GODADDY_DOMAIN("Domain"),

    HARBOR_PROJECT("Harbor Project"),
    HARBOR_REPOSITORY("Harbor Repository");

    private final String displayName;

    EdsAssetTypeEnum(String displayName) {
        this.displayName = displayName;
    }

}
