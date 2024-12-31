package com.baiyi.cratos.common.exception.constant;

/**
 * @Author baiyi
 * @Date 2024/3/15 15:04
 * @Version 1.0
 */
public interface ErrorCodeConstants {

    int AUTHENTICATION_401 = 401;

    int AUTHORIZATION_403 = 403;

    int BASE_ERROR = 999;

    int SQL_ERROR = 10000;

    int BUSINESS_ERROR = 40000;

    int CRED_ERROR = 41000;

    int CUSTOM_SCHEDULER_ERROR = 42000;

    int INVALID_CREDENTIAL_ERROR = 43000;

    int USER_ERROR = 44000;

    int TRAFFIC_LAYER_ERROR = 45000;

    int MENU_ERROR = 46000;

    int RISK_EVENT_ERROR = 47000;

    int ASSET_TO_BUSINESS_ERROR = 48000;

    int GLOBAL_NETWORK_ERROR = 49000;

    int ROBOT_ERROR = 50000;

    int KUBERNETES_RESOURCE_TEMPLATE_ERROR = 51000;

    int APPLICATION_ERROR = 52000;

    int BEETL_ERROR = 53000;

    int APPLICATION_RESOURCE_BASELINE_ERROR = 54000;
}
