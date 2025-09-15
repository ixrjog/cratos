package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 17:20
 * &#064;Version 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
@NoArgsConstructor(access = PRIVATE)
public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnpqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    // "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=";

    public static String generatePassword() {
        return generatePassword(16, true, true, true, true);
    }

    /**
     * 只包含小写字符和数字,长度为8
     *
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
        if (!(includeLowercase || includeUppercase || includeDigits || includeSpecialChars)) {
            throw new IllegalArgumentException("At least one character type must be selected");
        }

        StringBuilder validChars = new StringBuilder();
        List<Character> requiredChars = new ArrayList<>(4);

        SecureRandom random = new SecureRandom();

        if (includeLowercase) {
            validChars.append(LOWERCASE);
            requiredChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        }
        if (includeUppercase) {
            validChars.append(UPPERCASE);
            requiredChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        }
        if (includeDigits) {
            validChars.append(DIGITS);
            requiredChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        if (includeSpecialChars) {
            validChars.append(SPECIAL_CHARS);
            requiredChars.add(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
        }

        if (length < requiredChars.size()) {
            throw new IllegalArgumentException(
                    "Password length must be at least " + requiredChars.size() + " to include all required character types");
        }

        List<Character> passwordChars = new ArrayList<>(length);
        passwordChars.addAll(requiredChars);

        IntStream.range(requiredChars.size(), length)
                .mapToObj(i -> validChars.charAt(random.nextInt(validChars.length())))
                .forEach(passwordChars::add);

        // 洗牌算法打乱顺序
        IntStream.iterate(passwordChars.size() - 1, i -> i > 0, i -> i - 1)
                .forEach(i -> {
                    int j = random.nextInt(i + 1);
                    char tmp = passwordChars.get(i);
                    passwordChars.set(i, passwordChars.get(j));
                    passwordChars.set(j, tmp);
                });

        StringBuilder password = new StringBuilder(length);
        for (char c : passwordChars) {
            password.append(c);
        }
        return password.toString();
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