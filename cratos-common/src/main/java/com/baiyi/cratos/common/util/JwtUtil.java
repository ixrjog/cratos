package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.configuration.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2026/5/9 14:05
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 签发 JWT
     */
    public String generateToken(String username) {
        return generateToken(username, UUID.randomUUID().toString());
    }

    public String generateToken(String username, String jti) {
        long now = System.currentTimeMillis();
        long exp = now + jwtConfig.getExpiration();

        String header = base64UrlEncode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", username);
        payload.put("jti", jti);
        payload.put("iat", now / 1000);
        payload.put("exp", exp / 1000);

        String payloadEncoded = base64UrlEncode(toJson(payload));
        String content = header + "." + payloadEncoded;
        String signature = sign(content);

        return content + "." + signature;
    }

    /**
     * 验证 JWT 签名和过期时间，返回 payload claims
     */
    public Map<String, Object> verifyAndParse(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        String content = parts[0] + "." + parts[1];
        String expectedSig = sign(content);
        if (!expectedSig.equals(parts[2])) {
            throw new SecurityException("JWT signature verification failed");
        }

        Map<String, Object> claims = parseJson(base64UrlDecode(parts[1]));

        Object expObj = claims.get("exp");
        if (expObj != null) {
            long exp = ((Number) expObj).longValue();
            if (System.currentTimeMillis() / 1000 > exp) {
                throw new SecurityException("JWT token expired");
            }
        }

        return claims;
    }

    /**
     * 判断是否为 JWT 格式
     */
    public static boolean isJwt(String token) {
        return token != null && token.chars().filter(c -> c == '.').count() == 2;
    }

    /**
     * 从 JWT 中提取 jti（不验签，仅解析）
     */
    public static String extractJti(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) return null;
        Map<String, Object> claims = parseJson(base64UrlDecode(parts[1]));
        Object jti = claims.get("jti");
        return jti != null ? jti.toString() : null;
    }

    private String sign(String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key = new SecretKeySpec(
                    jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] sig = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(sig);
        } catch (Exception e) {
            throw new RuntimeException("JWT sign failed", e);
        }
    }

    private static String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String base64UrlDecode(String data) {
        return new String(Base64.getUrlDecoder().decode(data), StandardCharsets.UTF_8);
    }

    private static String toJson(Map<String, Object> map) {
        try {
            return MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            throw new RuntimeException("JSON serialize failed", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseJson(String json) {
        try {
            return MAPPER.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON parse failed", e);
        }
    }

}
