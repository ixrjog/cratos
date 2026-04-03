package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/10/8 10:53 上午
 * @Version 1.0
 */
@Getter
public enum DateFormatEnum {

    ISO8601("yyyy-MM-dd'T'HH:mm:ss'Z'"),
    ISO8601_SHORT("yyyy-MM-dd'T'HH:mm'Z'"),
    DATETIME("yyyy-MM-dd HH:mm:ss");

    private final String format;

    DateFormatEnum(String format) {
        this.format = format;
    }

}