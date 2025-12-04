package com.baiyi.cratos.workorder.enums;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/13 15:42
 * &#064;Version 1.0
 */
public interface TableHeaderConstants {

    String ALIYUN_DATAWORKS_AK = "| Aliyun Instance | AK RAM User |";
    String ALIYUN_KMS_SECRET_CREATE = "| Aliyun Instance | Secret Name | Version ID | Encryption Key ID | Config Center Value | Duplicate Secret | Description |";
    String ALIYUN_KMS_SECRET_UPDATE = "| Aliyun Instance | Secret Name | Version ID | Config Center Value |";
    String ALIYUN_ONS_CONSUMER_GROUP = "| Aliyun Instance | ONS Instance Name | Group ID | Delivery Order Type | Remark |";
    String ALIYUN_ONS_TOPIC = "| Aliyun Instance | ONS Instance Name | Topic Name | Message Type | Remark |";
    String ALIYUN_RAM_POLICY_PERMISSION = "| Aliyun Instance | RAM Login Username | Policy Name | Policy Type | Policy Desc |";
    String ALIYUN_RAM_USER_PERMISSION = "| Aliyun Instance | RAM Login Username | Login Link |";
    String ALIYUN_RAM_USER_RESET = "| Aliyun Instance | RAM Login Username | Reset Password | Unbind MFA | Login Link |";
    String AWS_IAM_POLICY_PERMISSION = "| AWS Instance | IAM Login Username | Policy Name | ARN |";
    String AWS_IAM_USER_RESET = "| Aws Instance | Account ID or alias | IAM Username | Login Link |";
    String AWS_TRANSFER_SFTP_USER_PERMISSION = "| Aws Instance | Transfer Username@Server | Key Fingerprint | Desc |";
    String AWS_IAM_USER_PERMISSION = "| AWS Instance | Account ID | IAM Login Username | Login Link |";
    String APPLICATION_DELETE_POD = "| Application Name | Tags |";
    String APPLICATION_REDEPLOY = "| Application Name | Tags |";
    String DEPLOYMENT_DELETE_POD = "| Instance Name | Namespace | Deployment Name | Pod Name | Delete Operation Time |";
    String DEPLOYMENT_REDEPLOY = "| Instance Name | Namespace | Deployment Name | Redeploy Operation Time |";
    String REVOKE_USER_ACCOUNT_PERMISSION = "| Instance Name | Instance Type | Account Type | Account Name |";
    String ALIMAIL_USER_RESET = "| Alimail Instance | User ID | Mail | Login URL |";
    String GITLAB_GROUP_PERMISSION = "| Instance Name | Group Name | Group WebURL | Role |";
    String GITLAB_PROJECT_PERMISSION = "| Instance Name | Project SshURL | Role |";

    String LDAP_ROLE_PERMISSION = "| LDAP Role | Description |";
    String REVOKE_USER_PERMISSION = "| Username | Name | DisplayName | Email | Tags |";
    String USER_RESET_PASSWORD = "| Username | Name | Display Name | Mail |";
    String APPLICATION_ELASTIC_SCALING = "| Application Name | Namespace | Current Replicas | Expected Replicas | Scaling Type |";
    String APPLICATION_FRONTEND_CREATE = "| Application Name | Type | Level | Repository SSH URL | Web Site |";
    String APPLICATION_DEPLOYMENT_SCALE = "| Instance Name | Namespace | Deployment | Current Replicas | Expected Replicas |";
    String RISK_CHANGE = "| Applicant | Title |";

}
