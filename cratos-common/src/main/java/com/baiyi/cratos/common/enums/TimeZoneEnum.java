package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2021/10/8 10:53 上午
 * @Version 1.0
 */
@Getter
public enum TimeZoneEnum {

    /**
     * UTC
     */
    UTC("yyyy-MM-dd'T'HH:mm:ss'Z'"),
    DEF("yyyy-MM-dd HH:mm:ss");

    private final String format;

    TimeZoneEnum(String format) {
        this.format = format;
    }

}