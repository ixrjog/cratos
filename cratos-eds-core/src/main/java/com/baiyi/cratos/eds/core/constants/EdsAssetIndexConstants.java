package com.baiyi.cratos.eds.core.constants;

import com.baiyi.cratos.domain.constant.Global;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/5 下午2:04
 * &#064;Version 1.0
 */
public interface EdsAssetIndexConstants {

    String DOMAIN_NAME = "domain.name";

    String CLOUD_ACCESS_KEY_IDS = "cloud.access.key.ids";
    String CLOUD_LOGIN_PROFILE = "cloud.login.profile";

    String CLOUD_ACCOUNT_USERNAME = "cloud.account.username";

    String ALIYUN_ACR_INSTANCE_ID = "acr.instanceId";
    String ALIYUN_ACR_REPO_NAMESPACE = "acr.repo.namespace";

    String ALIYUN_ONS_INSTANCE_ID = "ons.instanceId";
    String ALIYUN_ONS_INSTANCE_VPC_ENDPOINT = "ons.instance.vpc.endpoint";
    String ALIYUN_ONS_INSTANCE_INTERNET_ENDPOINT = "ons.instance.internet.endpoint";


    String ALIYUN_ONS_CONSUMER_GROUP_ID = "ons.consumerGroupId";
    String ALIYUN_ONS_TOPIC_NAME = "ons.topicName";

    String ALIYUN_RAM_USERS = "ram.users";
    String ALIYUN_RAM_POLICIES = "ram.policies";

    String ALIYUN_REDIS_PRIVATE_IP = "redis.privateIp";
    String ALIYUN_REDIS_INSTANCE_CLASS = "redis.instanceClass";
    String ALIYUN_REDIS_CONNECTION_DOMAIN = "redis.connectionDomain";

    String ALIYUN_ALB_INSTANCE_URL = "aliyun.alb.instance.url";

    String ALIMAIL_DEPARTMENT_PARENT_ID = "alimail.department.parentId";
    String ALIMAIL_USER_DEPARTMENT_IDS = "alimail.user.department.ids";
    String ALIYUN_KMS_ENDPOINT = "aliyun.kms.endpoint";
    String ALIYUN_KMS_INSTANCE_ID = "aliyun.kms.instanceId";
    // 内容Ha
    String CONTENT_HASH = "content.hash";

    String ALIYUN_ARMS_APP_HOME = "aliyun.arms.app.home";

    /**
     * 用户邮箱
     */
    String USER_MAIL = "user.mail";
    String USER_MAIL_ALIAS = "user.mail.alias";
    /**
     * 用户头像
     */
    String USER_AVATAR = "user.avatar";

    String GCP_MEMBER_ROLES = "gcp.member.roles";

    String AZURE_DIRECTORY_ROLES = "azure.directory.roles";

    String HUAWEICLOUD_IAM_POLICIES = "iam.policies";

    String AWS_SNS_SUBSCRIPTION_ENDPOINT = "endpoint";
    String AWS_SNS_SUBSCRIPTION_TOPIC_ARN = "topic.arn";
    String AWS_SNS_SUBSCRIPTION_PROTOCOL = "protocol";
    String AWS_IAM_POLICIES = "iam.policies";

    String DINGTALK_DEPT_PARENT_ID = "dingtalk.department.parentId";
    String DINGTALK_USER_USERNAME = "dingtalk.username";
    String DINGTALK_USER_MOBILE = "dingtalk.mobile";
    String DINGTALK_USER_LEADER = "dingtalk.leader";
    String DINGTALK_USER_AVATAR = "dingtalk.avatar";
    String DINGTALK_USER_BOSS = "dingtalk.boss";
    String DINGTALK_USER_JOB_NUMBER = "dingtalk.jobNumber";
    String DINGTALK_MANAGER_USER_ID = "dingtalk.manager.userId";

    String ENV = "env";

    String KUBERNETES_APP_NAME = Global.APP_NAME;

    String KUBERNETES_NAMESPACE = "namespace";
    String KUBERNETES_SERVICE_SELECTOR = "kubernetes.service.selector";

    String KUBERNETES_REPLICAS = "replicas";
    String KUBERNETES_GROUP = "deployment.spec.template.metadata.labels.group";
    String KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME = "loadBalancer.ingress.hostname";
    String KUBERNETES_INGRESS_SOURCE_IP = "alb.ingress.kubernetes.io/conditions.source-ip";
    String KUBERNETES_INGRESS_TRAFFIC_LIMIT_QPS = "alb.ingress.kubernetes.io/traffic-limit-qps";
    String KUBERNETES_INGRESS_ORDER = "alb.ingress.kubernetes.io/order";

    String KUBERNETES_NODE_CPU = "status.capacity.cpu";
    String KUBERNETES_NODE_CAPACITY_EPHEMERAL_STORAGE = "status.capacity.ephemeral-storage";
    String KUBERNETES_NODE_CAPACITY_MEMORY = "status.capacity.memory";

    String VPC_ID = "vpc.id";
    String VPC_CIDR_BLOCK = "vpc.cidr-block";
    String VIRTUAL_SWITCH_CIDR_BLOCK = "virtual-switch.cidr-block";
    String SUBNET_CIDR_BLOCK = "subnet.cidr-block";
    String SUBNET_AVAILABLE_IP_ADDRESS_COUNT = "subnet.availableIpAddressCount";
    String VPC_CIDRS = "vpc.cidrs";
    // EIP
    String EIP = "eip";

    String REPO_SSH_URL = "repo.sshUrl";
    String REPO_HTTP_URL = "repo.httpUrl";
    String REPO_WEB_URL = "repo.webUrl";

    //LDAP
    String LDAP_USER_GROUPS = "ldap.user.groups";
    String LDAP_USER_DN = "ldap.user.dn";

    String LDAP_GROUP_DN = "ldap.group.dn";
    String LDAP_GROUP_MEMBERS = "ldap.group.members";

    String GITLAB_USER_ID = "gitlab.user.id";
    String GITLAB_PROJECT_ID = "gitlab.project.id";
    String GITLAB_GROUP_ID = "gitlab.group.id";

    String CLOUDFLARE_ZONE_CNAME_SUFFIX = "cloudflare.zone.cname.suffix";
    String CLOUDFLARE_ZONE_DNS_RECORD_PROXIED = "cloudflare.zone.dns.record.proxied";
    String CLOUDFLARE_ZONE_DNS_RECORD_CONTENT = "cloudflare.zone.dns.record.content";

    String COUNTRYCODE = "countrycode";

}
