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

    private static final java.util.regex.Pattern LABEL_PATTERN = java.util.regex.Pattern.compile(
            "[A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?");

    /**
     * 判断是否为有效的域名(主机名)。
     * 规则:
     * - 总长度 ≤ 253,允许末尾根域点(FQDN);
     * - 由 '.' 分隔的多个标签,至少两段(如 example.com);
     * - 每个标签 1~63 字符,只含字母/数字/连字符,且不能以连字符开头或结尾;
     * - 顶级域(最后一段)必须是 ≥2 位的纯字母(排除 IP、全数字 TLD)。
     *
     * @param domainName 待校验字符串
     * @return 是否为有效域名
     */
    public static boolean isValidDomainName(String domainName) {
        if (domainName == null) {
            return false;
        }
        // 去掉末尾的根域点(FQDN 形式)
        String name = domainName.endsWith(".") ? domainName.substring(0, domainName.length() - 1) : domainName;
        if (name.isEmpty() || name.length() > 253) {
            return false;
        }
        String[] labels = name.split("\\.", -1);
        if (labels.length < 2) {
            return false;
        }
        for (String label : labels) {
            if (label.length() < 1 || label.length() > 63) {
                return false;
            }
            if (!LABEL_PATTERN.matcher(label)
                    .matches()) {
                return false;
            }
        }
        // 顶级域必须为 ≥2 位纯字母
        String tld = labels[labels.length - 1];
        return tld.length() >= 2 && tld.chars()
                .allMatch(Character::isLetter);
    }


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
