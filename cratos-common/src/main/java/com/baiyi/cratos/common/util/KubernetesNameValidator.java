package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/21 10:44
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class KubernetesNameValidator {

    // 正则表达式：以字母或数字开头，中间可以有连字符，以字母或数字结尾，总长度不超过253
    private static final String K8S_NAME_REGEX = "^[a-z0-9]([a-z0-9\\-]*[a-z0-9])?$";
    private static final Pattern K8S_NAME_PATTERN = Pattern.compile(K8S_NAME_REGEX);
    private static final int MAX_LENGTH = 253;

    /**
     * 验证Kubernetes资源名称是否合规
     *
     * @param name 要验证的资源名称
     * @return 如果合规返回true，否则返回false
     */
    public static boolean isResourceName(String name) {
        if (name == null || name.isEmpty() || name.length() > MAX_LENGTH) {
            return false;
        }
        Matcher matcher = K8S_NAME_PATTERN.matcher(name);
        return matcher.matches();
    }

}
