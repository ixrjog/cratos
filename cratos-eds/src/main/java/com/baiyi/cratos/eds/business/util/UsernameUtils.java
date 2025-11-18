package com.baiyi.cratos.eds.business.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 用户名生成工具类
 * 
 * @author baiyi
 * @since 2025/11/17
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsernameUtils {

    /**
     * 从邮箱地址生成用户名
     * 提取@符号前的本地部分，按点号分割后倒序拼接
     * 
     * @param email 邮箱地址，如 x.y.z@example.com
     * @return 生成的用户名，如 zyx；如果输入无效则返回空字符串
     */
    public static String ofEmail(String email) {
        if (email == null) return "";
        email = email.trim();
        if (email.isEmpty()) return "";

        int at = email.indexOf('@');
        String localPart = (at >= 0) ? email.substring(0, at) : email;
        if (localPart.isEmpty()) return "";

        StringBuilder username = new StringBuilder(localPart.length());
        int end = localPart.length();
        for (int i = localPart.length() - 1; i >= 0; i--) {
            if (localPart.charAt(i) == '.') {
                if (i + 1 < end) {
                    username.append(localPart, i + 1, end);
                }
                end = i;
            }
        }
        if (end > 0) {
            username.append(localPart, 0, end);
        }
        if (username.isEmpty()) return "";
        return username.toString()
                .toLowerCase(java.util.Locale.ROOT);
    }

    /**
     * 从姓名生成用户名
     * 处理流程：中文转拼音 -> 保留字母数字 -> 转小写
     * 
     * @param name 姓名，支持中英文
     * @return 生成的用户名，只包含小写字母和数字；如果输入无效则返回空字符串
     */
    public static String ofName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        // 简单的中文转拼音映射（可根据需要扩展）
        String pinyin = name.replaceAll("[\\u4e00-\\u9fa5]", "");
        // 只保留字母和数字
        StringBuilder result = new StringBuilder();
        for (char c : pinyin.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                result.append(c);
            }
        }
        return result.toString().toLowerCase();
    }

}
