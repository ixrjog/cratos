package com.baiyi.cratos.eds.zabbix.util;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/7 13:52
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxAlertUtils {

    public static boolean matchRule(EdsZabbixConfigModel.Zabbix zbx, String name) {
        List<String> rules = Optional.of(zbx)
                .map(EdsZabbixConfigModel.Zabbix::getAlert)
                .map(EdsZabbixConfigModel.Alert::getSilencing)
                .map(EdsZabbixConfigModel.Silencing::getRules)
                .orElse(List.of());
        if (CollectionUtils.isEmpty(rules)) {
            return false;
        }
        return rules.stream()
                .anyMatch(rule -> matchPattern(rule, name));
    }

    /**
     * 匹配模式规则
     *
     * @param pattern 规则模式，支持 * 通配符
     * @param text    待匹配的文本
     * @return 是否匹配
     */
    private static boolean matchPattern(String pattern, String text) {
        if (pattern == null || text == null) {
            return false;
        }
        if (pattern.equals("*")) {
            return true;
        }
        // 精确匹配（无通配符）
        if (!pattern.contains("*")) {
            return pattern.equals(text);
        }
        // 将模式转换为正则，转义除 '*' 之外的正则元字符，'*' 替换为 ".*"
        StringBuilder regex = new StringBuilder();
        regex.append('^');
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '*') {
                regex.append(".*");
            } else {
                // 转义正则元字符
                if ("\\.[]{}()+-?^$|".indexOf(c) >= 0) {
                    regex.append('\\');
                }
                regex.append(c);
            }
        }
        regex.append('$');
        return text.matches(regex.toString());
    }

}
