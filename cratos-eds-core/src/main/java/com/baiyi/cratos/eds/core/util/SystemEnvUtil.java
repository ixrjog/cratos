package com.baiyi.cratos.eds.core.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2021/6/25 5:30 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SystemEnvUtil {

    public static String renderEnvHome(String str) {
        try {
            return str.replace("${HOME}", System.getenv("HOME"));
        } catch (Exception ignored) {
        }
        return str;
    }

}