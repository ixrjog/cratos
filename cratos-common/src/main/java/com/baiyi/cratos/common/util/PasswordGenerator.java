package com.baiyi.cratos.common.util;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 17:20
 * &#064;Version 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnpqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
    private static final String DIGITS = "23456789";
    // "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=";

    public static String generatePassword() {
        return generatePassword(16, true, true, true, true);
    }

    /**
     * 只包含小写字符和数字,长度为8
     * @return
     */
    public static String generateTicketNo() {
        return generatePassword(8, true, false, true, false);
    }

    public static String generateMailPassword() {
        return generatePassword(8, true, true, true, true);
    }

    public static String generatePassword(int length, boolean includeLowercase, boolean includeUppercase,
                                          boolean includeDigits, boolean includeSpecialChars) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be greater than 0");
        }
        // 构建有效字符集
        StringBuilder validChars = new StringBuilder();
        if (includeLowercase) validChars.append(LOWERCASE);
        if (includeUppercase) validChars.append(UPPERCASE);
        if (includeDigits) validChars.append(DIGITS);
        if (includeSpecialChars) validChars.append(SPECIAL_CHARS);

        if (validChars.isEmpty()) {
            throw new IllegalArgumentException("At least one character type must be selected");
        }
        // 使用 SecureRandom 生成随机密码
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, length)
                .map(i -> random.nextInt(validChars.length()))
                .mapToObj(randomIndex -> String.valueOf(validChars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }

    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false; // 密码长度不足
        }
        boolean hasLowercase = password.chars()
                .anyMatch(ch -> LOWERCASE.indexOf(ch) != -1);
        boolean hasUppercase = password.chars()
                .anyMatch(ch -> UPPERCASE.indexOf(ch) != -1);
        boolean hasDigit = password.chars()
                .anyMatch(ch -> DIGITS.indexOf(ch) != -1);
        boolean hasSpecialChar = password.chars()
                .anyMatch(ch -> SPECIAL_CHARS.indexOf(ch) != -1);
        // 密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符
        return hasLowercase && hasUppercase && hasDigit && hasSpecialChar;
    }

}