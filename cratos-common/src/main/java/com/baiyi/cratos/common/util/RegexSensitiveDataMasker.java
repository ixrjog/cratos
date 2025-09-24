package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/7 14:54
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RegexSensitiveDataMasker {

    // 定义各种敏感信息的正则表达式
    private static final String PHONE_PATTERN = "\\b(?:\\+\\d{1,4}\\s?)?(?:\\(\\d{1,4}\\)\\s?)?\\d{7,15}\\b";
    private static final String ID_CARD_PATTERN = "\\b[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]\\b";
    private static final String BANK_CARD_PATTERN = "\\b\\d{16,19}\\b";
    private static final String EMAIL_PATTERN = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
    private static final String PASSWORD_PATTERN = "(?i)\"(password|pwd|密码)\"\\s*:\\s*\"[^\"]{6,}\"";
    private static final String LONG_NUMBER_PATTERN = "\\b\\d{11,}\\b";
    private static final String LONG_ALPHANUMERIC_PATTERN = "\\b[A-Za-z0-9]{16,}\\b";

    // 密钥相关的正则表达式
    private static final String AWS_ACCESS_KEY_PATTERN = "\\b(AKIA|ASIA)[0-9A-Z]{16}\\b";
    private static final String AWS_SECRET_KEY_PATTERN = "\\b[0-9a-zA-Z/+]{40}\\b";
    private static final String GENERIC_API_KEY_PATTERN = "(?i)\\b(api[_-]?key|app[_-]?key|secret|access[_-]?key)\\s*[:=]\\s*[\"']?[0-9a-zA-Z_\\-\\.]{16,64}[\"']?\\b";
    private static final String JWT_TOKEN_PATTERN = "\\beyJ[a-zA-Z0-9_-]{10,}\\.eyJ[a-zA-Z0-9_-]{10,}\\.[a-zA-Z0-9_-]{10,}\\b";
    private static final String PRIVATE_KEY_PATTERN = "-----BEGIN\\s+(?:RSA|DSA|EC|OPENSSH|PRIVATE)\\s+KEY-----[\\s\\S]*?-----END\\s+(?:RSA|DSA|EC|OPENSSH|PRIVATE)\\s+KEY-----";

    // Token相关的正则表达式
    private static final String TOKEN_PATTERN = "(?i)\\b(token|access_token|auth_token|bearer|oauth_token)\\s*[:=]\\s*[\"']?[0-9a-zA-Z_\\-\\.]{8,}[\"']?\\b";
    private static final String STANDALONE_TOKEN_PATTERN = "\\b(?!\\d{16,19}\\b)[0-9a-f]{32,128}\\b"; // 匹配可能是token的长字符串
    private static final String OAUTH_TOKEN_PATTERN = "\\b(?:ya29\\.|ghp_|gho_|github_pat_|sk-|xoxp-|xoxb-|xoxa-)[a-zA-Z0-9_\\-]{10,100}\\b";

    /**
     * 使用正则表达式脱敏字符串中的敏感信息
     */
    public static String maskSensitiveData(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String result = input;
        // 脱敏国际化手机号
        result = maskWithRegex(result, PHONE_PATTERN, phone -> {
            // 保留国家代码(如果有)和前缀，脱敏中间部分，保留最后4位
            int length = phone.length();
            // 处理国家代码
            int startIndex = 0;
            if (phone.startsWith("+")) {
                // 找到国家代码结束位置
                int i = 1;
                while (i < phone.length() && (Character.isDigit(phone.charAt(i)) || phone.charAt(
                        i) == ' ' || phone.charAt(i) == '(' || phone.charAt(i) == ')')) {
                    i++;
                }
                startIndex = i;
            }
            // 如果号码长度足够，保留前缀和后4位
            if (length - startIndex > 7) {
                int prefixLength = Math.min(3, (length - startIndex - 4) / 2);
                String prefix = phone.substring(0, startIndex + prefixLength);
                String suffix = phone.substring(length - 4);
                return prefix + "*".repeat(Math.max(0, length - startIndex - prefixLength - 4)) + suffix;
            } else {
                // 号码太短，只显示前2位和后2位
                return phone.substring(0, Math.min(2, length / 2)) + "*".repeat(
                        Math.max(0, length - 4)) + phone.substring(Math.max(0, length - 2));
            }
        });
        // 脱敏身份证
        result = maskWithRegex(result, ID_CARD_PATTERN, idCard -> idCard.substring(0, 4) + "**********" + idCard.substring(idCard.length() - 4));
        // 脱敏银行卡
        result = maskWithRegex(result, BANK_CARD_PATTERN, bankCard -> "****" + bankCard.substring(bankCard.length() - 4));
        // 脱敏邮箱
        result = maskWithRegex(result, EMAIL_PATTERN, email -> {
            int atIndex = email.indexOf('@');
            String prefix = email.substring(0, atIndex);
            String domain = email.substring(atIndex);
            if (prefix.length() <= 2) {
                return prefix + domain;
            }
            return prefix.charAt(0) + "****" + prefix.charAt(prefix.length() - 1) + domain;
        });

        // 脱敏长数字（超过10位的纯数字）
        result = maskWithRegex(result, LONG_NUMBER_PATTERN, number -> {
            if (number.length() <= 10) {
                return number;
            }
            return number.substring(0, 2) + "*".repeat(number.length() - 4) + number.substring(number.length() - 2);
        });

        // 脱敏长度超过16位的字母数字混合字符串
        result = maskWithRegex(result, LONG_ALPHANUMERIC_PATTERN, text -> {
            // 避免与其他已定义的模式重复处理
            if (text.matches("\\d+")) {
                return text; // 纯数字已由LONG_NUMBER_PATTERN处理
            }
            return text.substring(0, 2) + "*".repeat(text.length() - 4) + text.substring(text.length() - 2);
        });

        // 脱敏密码
        result = maskWithRegex(result, PASSWORD_PATTERN, passwordField -> {
            // 找到最后一个引号的位置，替换引号内的内容
            int lastQuoteIndex = passwordField.lastIndexOf('"');
            int secondLastQuoteIndex = passwordField.lastIndexOf('"', lastQuoteIndex - 1);
            if (secondLastQuoteIndex != -1 && lastQuoteIndex != -1) {
                return passwordField.substring(0, secondLastQuoteIndex + 1) + "********" + passwordField.substring(lastQuoteIndex);
            }
            return passwordField;
        });
        // 脱敏AWS访问密钥
        result = maskWithRegex(result, AWS_ACCESS_KEY_PATTERN, key -> key.substring(0, 4) + "**************");
        // 脱敏AWS秘密密钥
        result = maskWithRegex(result, AWS_SECRET_KEY_PATTERN, key -> "****************************************");
        // 脱敏通用API密钥
        result = maskWithRegex(result, GENERIC_API_KEY_PATTERN, keyField -> {
            // 找到密钥值的部分
            int colonIndex = Math.max(keyField.indexOf(':'), keyField.indexOf('='));
            if (colonIndex == -1) return keyField;

            String prefix = keyField.substring(0, colonIndex + 1);
            return prefix + " \"**********\"";
        });
        // 脱敏JWT令牌
        result = maskWithRegex(result, JWT_TOKEN_PATTERN, token -> {
            String[] parts = token.split("\\.");
            if (parts.length >= 3) {
                return parts[0].substring(0, Math.min(parts[0].length(), 6)) + "..." + "." + parts[1].substring(0,
                        Math.min(parts[1].length(), 6)) + "..." + "." + parts[2].substring(0,
                        Math.min(parts[2].length(), 6)) + "...";
            }
            return "**********";
        });
        // 脱敏私钥
        result = maskWithRegex(result, PRIVATE_KEY_PATTERN, privateKey -> {
            String[] lines = privateKey.split("\n");
            if (lines.length >= 2) {
                return lines[0] + "\n******内容已脱敏******\n" + lines[lines.length - 1];
            }
            return "**********";
        });
        // 脱敏带标识的token
        result = maskWithRegex(result, TOKEN_PATTERN, tokenField -> {
            // 找到token值的部分
            int colonIndex = Math.max(tokenField.indexOf(':'), tokenField.indexOf('='));
            if (colonIndex == -1) return tokenField;
            String prefix = tokenField.substring(0, colonIndex + 1);
            return prefix + " \"**********\"";
        });
        // 脱敏OAuth和特定格式的token
        result = maskWithRegex(result, OAUTH_TOKEN_PATTERN, token -> {
            // 保留前缀标识符和前几个字符
            int prefixEndIndex = token.indexOf('_') + 1;
            if (prefixEndIndex > 0) {
                return token.substring(0, prefixEndIndex) + "**********";
            } else {
                // 如果没有下划线，保留前4个字符
                return token.substring(0, Math.min(4, token.length())) + "**********";
            }
        });
        // 脱敏可能是token的独立长字符串
        result = maskWithRegex(result, STANDALONE_TOKEN_PATTERN, token -> token.substring(0, Math.min(4, token.length())) + "**********");
        return result;
    }

    /**
     * 使用正则表达式和自定义脱敏函数处理字符串
     */
    private static String maskWithRegex(String input, String regex,
                                        java.util.function.Function<String, String> maskFunction) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String matched = matcher.group();
            String masked = maskFunction.apply(matched);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(masked));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
