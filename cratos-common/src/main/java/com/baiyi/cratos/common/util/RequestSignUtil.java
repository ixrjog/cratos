package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static lombok.AccessLevel.PRIVATE;

/**
 * 请求签名工具：Jti + Timestamp + Content-Length，以 token 为密钥做 HMAC-SHA256
 *
 * @Author baiyi
 * @Date 2026/5/9 14:42
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class RequestSignUtil {

    private static final long TIMESTAMP_TOLERANCE_MS = 30_000L;

    /**
     * 生成签名（原始数据）
     */
    public static String sign(String signData, String token) {
        return hmacSha256(signData, token);
    }

    /**
     * 生成签名（含 contentLength）
     */
    public static String sign(String jti, String timestamp, String contentLength, String token) {
        String data = jti + timestamp + contentLength;
        return hmacSha256(data, token);
    }

    /**
     * 生成签名（不含 contentLength）
     */
    public static String sign(String jti, String timestamp, String token) {
        String data = jti + timestamp;
        return hmacSha256(data, token);
    }

    /**
     * 验证签名（通用）
     */
    public static boolean verify(String signData, String token, String jwtSign) {
        String expected = hmacSha256(signData, token);
        return expected.equals(jwtSign);
    }

    /**
     * 验证签名（含 contentLength）
     */
    public static boolean verify(String jti, String timestamp, String contentLength, String token, String jwtSign) {
        String expected = sign(jti, timestamp, contentLength, token);
        return expected.equals(jwtSign);
    }

    /**
     * 验证签名（不含 contentLength）
     */
    public static boolean verify(String jti, String timestamp, String token, String jwtSign) {
        String expected = sign(jti, timestamp, token);
        return expected.equals(jwtSign);
    }

    public static String sha256Hex(String data) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 检查时间戳是否在允许窗口内（±30s）
     */
    public static boolean isTimestampValid(String timestamp) {
        try {
            long ts = Long.parseLong(timestamp);
            long now = System.currentTimeMillis();
            return Math.abs(now - ts) <= TIMESTAMP_TOLERANCE_MS;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String hmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 sign failed", e);
        }
    }

}
