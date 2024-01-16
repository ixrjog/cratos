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
    OK(0, "成功"), SYSTEM_ERROR(10001, "系统错误！"),
    ENCRYPTION_AND_DECRYPTION_ERROR(10002, "Encryption and decryption errors."),

    AUTHENTICATION_FAILED(401, "Authentication failed."),
    INVALID_TOKEN(401,"Invalid token."),
    TOKEN_EXPIRED(401,"Token expired, please log in again."),

    // authentication
    INVALID_IDENTITY_AUTHENTICATION_PROVIDER_CONFIGURATION(9999, "Invalid identity authentication provider configuration.");

    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}