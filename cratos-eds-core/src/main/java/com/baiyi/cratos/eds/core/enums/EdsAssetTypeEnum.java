package com.baiyi.cratos.eds.core.enums;

import com.baiyi.cratos.eds.core.annotation.CloudComputer;
import com.baiyi.cratos.eds.core.annotation.CloudIdentity;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author baiyi
 * &#064;Date 2024/2/26 13:48
 * &#064;Version 1.0
 */
@Getter
public enum EdsAssetTypeEnum {
    @CloudComputer CRATOS_COMPUTER("Computer", 1),

    ALIYUN_ALB("ALB", 2),
    ALIYUN_NLB("NLB", 3),
    @CloudComputer ALIYUN_ECS("ECS", 4),
    ALIYUN_DOMAIN("Domain", 5),
    @CloudIdentity ALIYUN_RAM_USER("RAM User", 6),
    ALIYUN_RAM_POLICY("RAM Policy", 7),
    ALIYUN_RAM_ACCESS_KEY("RAM AccessKey", 8),
    ALIYUN_OSS_BUCKET("OSS Bucket", 9),
    ALIYUN_ARMS_TRACE_APPS("ARMS Trace Apps", 10),
    ALIYUN_DDS_MONGO_INSTANCE("MongoDB", 11),
    ALIYUN_ONS_V5_INSTANCE("ONS(5.0) Instance", 12),
    ALIYUN_ONS_V5_TOPIC("ONS(5.0) Topic", 13),
    ALIYUN_ONS_V5_CONSUMER_GROUP("ONS(5.0) ConsumerGroup", 14),
    ALIYUN_ONS_V5_CONSUMER_GROUP_SUBSCRIPTION("ONS(5.0) Subscription", 15),
    ALIYUN_ACR_NAMESPACE("ACR Namespace", 16),
    ALIYUN_ACR_INSTANCE("ACR Instance", 17),
    ALIYUN_ACR_REPOSITORY("ACR Repository", 18),
    ALIYUN_REDIS("Redis", 19),
    ALIYUN_RDS_INSTANCE("RDS Instance", 20),
    ALIYUN_RDS_DATABASE("RDS DB", 21),
    ALIYUN_VPC("VPC", 22),
    ALIYUN_VIRTUAL_SWITCH("vSwitch", 23),
    ALIYUN_KMS_INSTANCE("KMS Instance", 24),
    ALIYUN_KMS_SECRET("KMS Secret", 25),
    ALIYUN_KMS_KEY("KMS Key", 26),
    ALIYUN_DMS_USER("DMS User", 27),
    ALIYUN_CERT("Certificate", 28),

    ALIMAIL_DEPARTMENT("Department", 29),
    ALIMAIL_USER("User", 30),

    AWS_CERT("Certificate", 31),
    AWS_STS_VPN("Site-to-Site VPN", 32),
    AWS_ELB("ELB", 33),
    @CloudComputer AWS_EC2("EC2", 34),
    AWS_EBS("EBS", 35),
    AWS_DOMAIN("Domain", 36),
    AWS_HOSTED_ZONE("Hosted Zone", 37),
    AWS_SQS_QUEUE("SQS Queue", 38),
    AWS_SNS_TOPIC("SNS Topic", 39),
    AWS_SNS_SUBSCRIPTION("SNS Subscription", 40),
    AWS_TRANSFER_SERVER("Transfer Server", 41),
    AWS_CLOUDFRONT_DISTRIBUTION("CloudFront Distribution", 42),
    AWS_S3_BUCKET("S3 Bucket", 43),
    AWS_ECR_REPOSITORY("ECR Repository", 44),
    AWS_VPC("VPC", 45),
    AWS_SUBNET("Subnet", 46),
    @CloudIdentity AWS_IAM_USER("IAM User", 47),
    AWS_IAM_POLICY("IAM Policy", 48),
    @CloudComputer HUAWEICLOUD_ECS("ECS", 49),
    @CloudIdentity HUAWEICLOUD_IAM_USER("IAM User", 50),
    HUAWEICLOUD_SCM_CERT("Certificate", 51),
    HUAWEICLOUD_VPC("VPC", 52),
    HUAWEICLOUD_SUBNET("Subnet", 53),

    CLOUDFLARE_ZONE("Zone", 54),
    CLOUDFLARE_CERT("Certificate", 55),
    CLOUDFLARE_DNS_RECORD("DNS Record", 56),

    KUBERNETES_NAMESPACE("Namespace", 57),
    KUBERNETES_DEPLOYMENT("Deployment", 58),
    KUBERNETES_INGRESS("Ingress", 59),
    KUBERNETES_SERVICE("Service", 60),
    KUBERNETES_NODE("Node", 61),
    KUBERNETES_POD("Pod", 62),
    KUBERNETES_VIRTUAL_SERVICE("Virtual Service", 63),
    KUBERNETES_DESTINATION_RULE("Destination Rule", 64),
    KUBERNETES_ENVOY_FILTER("EnvoyFilter", 65),
    KUBERNETES_ALIBABACLOUD_AUTOSCALER("Alibabacloud Autoscaler", 66),
    KUBERNETES_CONFIG_MAP("ConfigMap", 67),

    LDAP_PERSON("Person", 68),
    LDAP_GROUP("Group", 69),

    DINGTALK_USER("User", 70),
    DINGTALK_DEPARTMENT("Department", 71),
    DINGTALK_ROBOT_MSG("RobotMsg", 72),

    GITLAB_PROJECT("Project", 73),
    GITLAB_GROUP("Group", 74),
    GITLAB_USER("User", 75),
    GITLAB_SSHKEY("SSH Key", 76),
    GITLAB_SYSTEM_HOOK("System Hooks", 77),
    GANDI_DOMAIN("Domain", 78),

    GODADDY_DOMAIN("Domain", 79),

    HARBOR_PROJECT("Harbor Project", 80),
    HARBOR_REPOSITORY("Harbor Repository", 81),

    GCP_CERTIFICATE("Certificate", 82),
    @CloudIdentity GCP_MEMBER("Member", 83),

    JENKINS_COMPUTER("CloudComputer Node", 84),

    EAGLECLOUD_SASE_DATA_SECURITY_EVENT("Data Security Event", 0),
    EAGLECLOUD_SASE_DATA_SECURITY_ALERT_NOTIFICATION("Data Security Alert Notification", 1),
    EAGLECLOUD_SASE_DATA_SECURITY_ALERT_RECORD("Data Security Alert Record", 2),

    ZBX_HOST("Host", 85),
    ZBX_HOSTGROUP("HostGroup", 86),
    ZBX_TEMPLATE("Template", 87),
    ZBX_EVENT("Event", 88),
    @CloudIdentity AZURE_USER("User", 89),
    AZURE_DIRECTORY_ROLE("DirectoryRole", 90),

    DEF("Default", 9999);

    private final String displayName;
    private final Integer seq;

    EdsAssetTypeEnum(String displayName, Integer seq) {
        this.displayName = displayName;
        this.seq = seq;
    }

    public static final List<EdsAssetTypeEnum> CLOUD_IDENTITY_TYPES = getCloudIdentityTypes();

    private static List<EdsAssetTypeEnum> getCloudIdentityTypes() {
        return Arrays.stream(EdsAssetTypeEnum.values())
                .filter(assetType -> {
                    try {
                        Field field = EdsAssetTypeEnum.class.getField(assetType.name());
                        return field.isAnnotationPresent(CloudIdentity.class);
                    } catch (NoSuchFieldException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public static final List<EdsAssetTypeEnum> CLOUD_COMPUTER_TYPES = getCloudComputerTypes();

    private static List<EdsAssetTypeEnum> getCloudComputerTypes() {
        return Arrays.stream(EdsAssetTypeEnum.values())
                .filter(assetType -> {
                    try {
                        Field field = EdsAssetTypeEnum.class.getField(assetType.name());
                        return field.isAnnotationPresent(CloudComputer.class);
                    } catch (NoSuchFieldException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

}
