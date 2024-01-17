package com.baiyi.cratos.domain;

import lombok.Getter;


/**
 * @author liangjian
 */

@Getter
public enum ErrorEnum {

    /**
     * 错误
     */
    OK(0, "OK"),
    SYSTEM_ERROR(503, "Service Unavailable."),
    ENCRYPTION_AND_DECRYPTION_ERROR(10002, "Encryption and decryption errors."),

    // 认证
    AUTHENTICATION_FAILED(401, "Authentication failed."),
    AUTHENTICATION_REQUEST_NO_TOKEN(401, "No valid token was carried in the request."),
    AUTHENTICATION_INVALID_TOKEN(401,"Invalid token."),
    AUTHENTICATION_TOKEN_EXPIRED(401,"Token expired, please login again."),
    AUTHENTICATION_RESOURCE_NOT_EXIST(403, "The resource path does not exist."),
    AUTHENTICATION_INVALID_IDENTITY_AUTHENTICATION_PROVIDER_CONFIGURATION(401, "Invalid identity authentication provider configuration."),

    // 鉴权
    AUTHORIZATION_FAILURE(403, "Unauthorized access to current resources."),
    ;

    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}