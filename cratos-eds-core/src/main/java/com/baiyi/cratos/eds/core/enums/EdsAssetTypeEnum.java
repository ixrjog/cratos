package com.baiyi.cratos.eds.core.enums;

import lombok.Getter;

/**
 * &#064;Author baiyi
 * &#064;Date 2024/2/26 13:48
 * &#064;Version 1.0
 */
@Getter
public enum EdsAssetTypeEnum {

    ALIYUN_CERT("Certificate", 0),
    ALIYUN_ALB("ALB", 0),
    ALIYUN_ECS("ECS", 0),
    ALIYUN_DOMAIN("Domain", 0),
    ALIYUN_RAM_USER("RAM User", 10),
    ALIYUN_RAM_POLICY("RAM Policy", 12),
    ALIYUN_RAM_ACCESS_KEY("RAM AccessKey", 13),
    ALIYUN_OSS_BUCKET("OSS Bucket", 6),
    ALIYUN_ARMS_TRACE_APPS("ARMS Trace Apps", 7),
    ALIYUN_DDS_MONGO_INSTANCE("MongoDB", 8),
    ALIYUN_ONS_V5_INSTANCE("ONS(5.0) Instance", 20),
    ALIYUN_ONS_V5_TOPIC("ONS(5.0) Topic", 21),
    ALIYUN_ONS_V5_CONSUMER_GROUP("ONS(5.0) ConsumerGroup", 22),
    ALIYUN_ONS_V5_CONSUMER_GROUP_SUBSCRIPTION("ONS(5.0) Subscription", 23),
    ALIYUN_ACR_NAMESPACE("ACR Namespace", 30),
    ALIYUN_ACR_INSTANCE("ACR Instance", 31),
    ALIYUN_ACR_REPOSITORY("ACR Repository", 32),
    ALIYUN_REDIS("Redis", 0),
    ALIYUN_RDS_INSTANCE("RDS Instance", 41),
    ALIYUN_RDS_DATABASE("RDS DB", 42),
    ALIYUN_VPC("VPC", 43),
    ALIYUN_VIRTUAL_SWITCH("vSwitch", 44),

    ALIMAIL_DEPARTMENT("Department", 0),
    ALIMAIL_USER("User", 1),

    AWS_CERT("Certificate", 0),
    AWS_STS_VPN("Site-to-Site VPN", 0),
    AWS_ELB("ELB", 0),
    AWS_EC2("EC2", 10),
    AWS_EBS("EBS", 11),
    AWS_DOMAIN("Domain", 0),
    AWS_SQS_QUEUE("SQS Queue", 20),
    AWS_SNS_TOPIC("SNS Topic", 21),
    AWS_SNS_SUBSCRIPTION("SNS Subscription", 22),
    AWS_TRANSFER_SERVER("Transfer Server", 0),
    AWS_CLOUDFRONT_DISTRIBUTION("CloudFront Distribution", 0),
    AWS_S3_BUCKET("S3 Bucket", 0),
    AWS_ECR_REPOSITORY("ECR Repository", 0),
    AWS_VPC("VPC", 43),
    AWS_SUBNET("Subnet", 44),
    AWS_IAM_USER("IAM User", 45),
    AWS_IAM_POLICY("IAM Policy",46),

    HUAWEICLOUD_ECS("ECS", 0),
    HUAWEICLOUD_IAM_USER("IAM User", 0),
    HUAWEICLOUD_SCM_CERT("Certificate", 0),
    HUAWEICLOUD_VPC("VPC", 0),
    HUAWEICLOUD_SUBNET("Subnet", 0),

    CLOUDFLARE_CERT("Certificate", 0),

    KUBERNETES_NAMESPACE("Namespace", 0),
    KUBERNETES_DEPLOYMENT("Deployment", 0),
    KUBERNETES_INGRESS("Ingress", 0),
    KUBERNETES_SERVICE("Service", 0),
    KUBERNETES_NODE("Node", 0),
    KUBERNETES_VIRTUAL_SERVICE("Virtual Service", 0),
    KUBERNETES_DESTINATION_RULE("Destination Rule", 0),
    KUBERNETES_ENVOY_FILTER("EnvoyFilter", 0),
    KUBERNETES_ALIBABACLOUD_AUTOSCALER("Alibabacloud Autoscaler", 0),
    KUBERNETES_CONFIG_MAP("ConfigMap", 0),

    LDAP_PERSON("Person", 0),
    LDAP_GROUP("Group", 0),

    DINGTALK_USER("User", 0),
    DINGTALK_DEPARTMENT("Department", 0),
    DINGTALK_ROBOT_MSG("RobotMsg", 0),

    GITLAB_PROJECT("Project", 0),
    GITLAB_GROUP("Group", 0),
    GITLAB_USER("User", 0),
    GITLAB_SSHKEY("SSH Key", 0),
    GITLAB_SYSTEM_HOOK("System Hooks", 0),
    GANDI_DOMAIN("Domain", 0),

    GODADDY_DOMAIN("Domain", 0),

    HARBOR_PROJECT("Harbor Project", 0),
    HARBOR_REPOSITORY("Harbor Repository", 0),

    GCP_CERTIFICATE("Certificate", 0),
    GCP_MEMBER("Member", 0),

    DEF("Default", 0);

    private final String displayName;
    private final Integer seq;

    EdsAssetTypeEnum(String displayName, Integer seq) {
        this.displayName = displayName;
        this.seq = seq;
    }

}
