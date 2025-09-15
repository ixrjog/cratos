package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/1 10:31
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class VersionValidator {

    /**
     * 判断版本号是否符合 x.x.x 格式
     * @param version 待检查的版本字符串
     * @return 如果符合格式返回true，否则返回false
     */
    public static boolean isValidVersion(String version) {
        // 检查输入是否为null
        if (version == null) {
            return false;
        }

        // 使用正则表达式匹配 x.x.x 格式
        // 其中x是一个或多个数字
        return version.matches("^(0|[1-9]\\d{0,2})\\.(0|[1-9]\\d{0,2})\\.(0|[1-9]\\d{0,2})$");
    }

}
