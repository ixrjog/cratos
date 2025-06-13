package com.baiyi.cratos.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/12 11:30
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainUtils {

    public static String extractRegisteredDomain(String url) {
        String domain = url.trim();
        try {
            // 确保URL格式正确，如果没有协议部分，添加一个临时的
            if (!domain.startsWith("http://") && !domain.startsWith("https://")) {
                domain = "https://" + domain;
            }

            // 解析URL获取主机名
            URI uri = new URI(domain);
            String host = uri.getHost();

            if (host == null) {
                return domain; // 无法解析时返回原始输入
            }

            // 使用正则表达式匹配注册域名
            // 这个正则表达式匹配最后两个部分（二级域名和顶级域名）
            Pattern pattern = Pattern.compile("([\\w-]+\\.[a-z]{2,})$");
            Matcher matcher = pattern.matcher(host);

            if (matcher.find()) {
                return matcher.group(1);
            }

            // 处理更复杂的情况，如co.uk等
            pattern = Pattern.compile("([\\w-]+\\.[a-z]{2,3}\\.[a-z]{2})$");
            matcher = pattern.matcher(host);

            if (matcher.find()) {
                return matcher.group(1);
            }

            // 如果无法匹配，返回原始主机名
            return host;

        } catch (URISyntaxException e) {
            // 处理URL解析异常
            return domain;
        }
    }

}
