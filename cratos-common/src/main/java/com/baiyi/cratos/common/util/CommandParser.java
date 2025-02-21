package com.baiyi.cratos.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 14:51
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandParser {

    /**
     * 解析带双引号的命令字符串
     *
     * @param command 输入的命令字符串
     * @return 解析后的参数列表
     */
    public static List<String> parseCommand(String command) {
        List<String> params = new ArrayList<>();
        // 正则表达式匹配双引号内的内容或非空白字符
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            // 如果匹配到双引号内的内容，则取第一个分组
            if (matcher.group(1) != null) {
                params.add(matcher.group(1));
            }
            // 否则取第二个分组（非空白字符）
            else {
                params.add(matcher.group(2));
            }
        }
        return params;
    }

    public static String maskString(String str) {
        if (str == null || str.length() <= 4) {
            return str; // 如果字符串为空或长度小于等于3，直接返回原字符串
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str, 0, 4); // 添加前3个字符
        int maskLength = str.length() - 4; // 需要掩码的字符数量
        sb.append("*".repeat(maskLength)); // 添加掩码字符*
        return sb.toString();
    }

}