package com.baiyi.cratos.common.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        String URL_REGEX = "^https://[^\\s/$.?#].[^\\s]*$";
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

    public static boolean isEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return email.matches(RegexMatches.EMAIL);
    }

    public static boolean isURL(String url) {
        if (!StringUtils.hasText(url)) {
            return false;
        }
        Pattern pattern = Pattern.compile(RegexMatches.URL_REGEX);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean containsHyphenBetweenDigits(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        // 检查是否存在数字-数字的模式
        return phoneNumber.matches(".*\\d-\\d.*");
    }

}
