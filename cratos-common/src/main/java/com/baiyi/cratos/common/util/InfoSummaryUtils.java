package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static lombok.AccessLevel.PRIVATE;

/**
 * 信息摘要工具类
 * 提供各种哈希算法的实现
 *
 * @author baiyi
 */
@NoArgsConstructor(access = PRIVATE)
public class InfoSummaryUtils {

    public static final String SHA256 = "SHA-256";

    public static String toContentHash(String algorithm, String hash) {
        return "{" + algorithm + "}" + hash;
    }

    /**
     * 将字符串转换为 SHA256 哈希值
     *
     * @param input 输入字符串
     * @return SHA256 哈希值（十六进制字符串）
     */
    public static String toSHA256(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入字符串不能为 null");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 算法不可用", e);
        }
    }

    /**
     * 将字符串转换为 SHA256 哈希值（大写）
     *
     * @param input 输入字符串
     * @return SHA256 哈希值（大写十六进制字符串）
     */
    public static String toSHA256Upper(String input) {
        return toSHA256(input).toUpperCase();
    }

    /**
     * 将字符串转换为 SHA1 哈希值
     *
     * @param input 输入字符串
     * @return SHA1 哈希值（十六进制字符串）
     */
    public static String toSHA1(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入字符串不能为 null");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 算法不可用", e);
        }
    }

    /**
     * 将字符串转换为 MD5 哈希值
     *
     * @param input 输入字符串
     * @return MD5 哈希值（十六进制字符串）
     */
    public static String toMD5(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入字符串不能为 null");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 算法不可用", e);
        }
    }

    /**
     * 验证字符串的 SHA256 哈希值
     *
     * @param input        原始字符串
     * @param expectedHash 期望的哈希值
     * @return 是否匹配
     */
    public static boolean verifySHA256(String input, String expectedHash) {
        if (input == null || expectedHash == null) {
            return false;
        }

        String actualHash = toSHA256(input);
        return actualHash.equalsIgnoreCase(expectedHash);
    }

    /**
     * 计算字符串的简单哈希码（用于非安全场景）
     *
     * @param input 输入字符串
     * @return 哈希码
     */
    public static int simpleHash(String input) {
        if (input == null) {
            return 0;
        }
        return input.hashCode();
    }

    /**
     * 生成指定长度的摘要（截取 SHA256 结果）
     *
     * @param input  输入字符串
     * @param length 摘要长度
     * @return 指定长度的摘要
     */
    public static String generateDigest(String input, int length) {
        if (length <= 0 || length > 64) {
            throw new IllegalArgumentException("摘要长度必须在 1-64 之间");
        }

        String sha256 = toSHA256(input);
        return sha256.substring(0, Math.min(length, sha256.length()));
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * 计算多个字符串的组合哈希值
     *
     * @param inputs 输入字符串数组
     * @return SHA256 哈希值
     */
    public static String combineHash(String... inputs) {
        if (inputs == null || inputs.length == 0) {
            throw new IllegalArgumentException("输入参数不能为空");
        }

        StringBuilder combined = new StringBuilder();
        for (String input : inputs) {
            if (input != null) {
                combined.append(input);
            }
        }

        return toSHA256(combined.toString());
    }

    /**
     * 生成带盐值的哈希
     *
     * @param input 输入字符串
     * @param salt  盐值
     * @return 带盐值的 SHA256 哈希
     */
    public static String saltedHash(String input, String salt) {
        if (input == null) {
            throw new IllegalArgumentException("输入字符串不能为 null");
        }
        if (salt == null) {
            salt = "";
        }

        return toSHA256(input + salt);
    }

    /**
     * 检查字符串是否为有效的 SHA256 哈希值
     *
     * @param hash 待检查的字符串
     * @return 是否为有效的 SHA256 哈希值
     */
    public static boolean isValidSHA256(String hash) {
        if (hash == null) {
            return false;
        }

        // SHA256 哈希值长度为 64 个十六进制字符
        if (hash.length() != 64) {
            return false;
        }

        // 检查是否只包含十六进制字符
        return hash.matches("^[a-fA-F0-9]+$");
    }
}
