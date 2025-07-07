package com.baiyi.cratos.common.session;

import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/3 15:09
 * &#064;Version 1.0
 */
public class UserSessionContext {

    private static final ThreadLocal<String> LANGUAGE_CONTEXT = new ThreadLocal<>();

    public static void setLanguage(String language) {
        if (StringUtils.hasText(language)) {
            LANGUAGE_CONTEXT.set(language);
        }
    }

    public static String getLanguage() {
        return LANGUAGE_CONTEXT.get();
    }

    public static void remove() {
        LANGUAGE_CONTEXT.remove();
    }

}
