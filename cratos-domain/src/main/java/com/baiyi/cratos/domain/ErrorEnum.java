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
    OK(0, "成功");

    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}