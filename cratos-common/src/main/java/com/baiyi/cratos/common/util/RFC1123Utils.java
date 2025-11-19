package com.baiyi.cratos.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * RFC 1123 域名和主机名校验工具类
 * 
 * @Author baiyi
 * @Date 2025/11/19 09:52
 * @Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RFC1123Utils {

    /**
     * 资源名称正则：3-253字符，以字母开头，以字母数字结尾，中间可包含小写字母数字和连字符
     */
    private static final Pattern RESOURCE_NAME_PATTERN = Pattern.compile("^[a-z]([a-z0-9-]{1,251}[a-z0-9])?$");

    /**
     * 标签名称正则：1-63字符，以字母数字开头结尾，中间可包含小写字母数字和连字符
     */
    private static final Pattern LABEL_NAME_PATTERN = Pattern.compile("^[a-z0-9]([a-z0-9-]{0,61}[a-z0-9])?$");

    /**
     * 校验资源名称是否符合规范
     * 字符数字 >=3 and  <=253
     * 仅包含小写字母数字字符、'-'
     * 以字母字符开头
     * 以字母数字字符结尾
     * @param name 资源名称
     * @return 是否符合规范
     */
    public static boolean isValidNames(String name) {
        if (name == null || name.length() < 3 || name.length() > 253) {
            return false;
        }
        return RESOURCE_NAME_PATTERN.matcher(name).matches();
    }

    /**
     * 最多包含 63 个字符
     * 仅包含小写字母数字字符或“-”
     * 以字母开头
     * 以字母数字字符结尾
     * @param name
     * @return
     */
    public static boolean isValidLabelNames(String name) {
        if (name == null || name.isEmpty() || name.length() > 63) {
            return false;
        }
        // 检查是否以字母开头
        if (!Character.isLetter(name.charAt(0)) || !Character.isLowerCase(name.charAt(0))) {
            return false;
        }
        // 检查是否以字母数字结尾
        char lastChar = name.charAt(name.length() - 1);
        if (!Character.isLetterOrDigit(lastChar) || Character.isUpperCase(lastChar)) {
            return false;
        }
        // 检查所有字符是否符合规范
        for (char c : name.toCharArray()) {
            if (!Character.isLowerCase(c) && !Character.isDigit(c) && c != '-') {
                return false;
            }
        }
        return true;
    }

}
