package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static lombok.AccessLevel.PRIVATE;

/**
 * SHA-256 摘要工具类
 *
 * @Author baiyi
 * @Date 2026/5/21
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class Sha256Util {

    private static final String ALGORITHM = "SHA-256";

    /**
     * 计算字符串的 SHA-256，返回小写十六进制
     */
    public static String hash(String data) {
        return hashBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的 SHA-256，返回小写十六进制
     */
    public static String hashBytes(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] digest = md.digest(data);
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
