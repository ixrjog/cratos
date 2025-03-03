package com.baiyi.cratos.common.util;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/3 14:33
 * &#064;Version 1.0
 */
public class ValidationUtils {

    private interface RegexMatches {
        String PHONE = "^1[3456789]\\d{9}$";
        String USERNAME = "[a-zA-Z][\\d0-9a-zA-Z-_]{3,64}";
        String SERVER_NAME = "[a-zA-Z][\\d0-9a-zA-Z-.]{1,55}";
        String SERVER_GROUP_NAME = "group_[a-z][\\d0-9a-z-]{2,64}";
        String APPLICATION_NAME = "[a-z][\\d0-9a-z-]{3,32}";
        String EMAIL = "[A-z0-9-_.]+@(\\w+[\\w-]*)(\\.\\w+[-\\w]*)+";
        String JOB_KEY = "[a-z0-9-_]{3,64}";
    }

    /**
     * 校验字符串是否为手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        return phone.matches(RegexMatches.PHONE);
    }

}
