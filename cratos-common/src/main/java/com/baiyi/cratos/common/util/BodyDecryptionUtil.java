package com.baiyi.cratos.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Body 解密工具类
 * 用于解密前端发送的加密 Body（混合加密：RSA + AES-GCM）
 *
 * @Author baiyi
 * @Date 2026/01/23
 */
@Slf4j
public class BodyDecryptionUtil {

    private static final String RSA_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;

    /**
     * 解密 Body
     *
     * @param encryptedBody 加密的 Body（Base64(IV).Base64(Ciphertext)）
     * @param encryptedKey  加密的 AES 密钥（Base64）
     * @param privateKeyPem RSA 私钥（PEM 格式）
     * @return 解密后的 JSON 字符串
     */
    public static String decryptBody(String encryptedBody, String encryptedKey, String privateKeyPem) {
        try {
            // 1. 用 RSA 私钥解密 AES 密钥
            byte[] aesKeyBytes = decryptWithRSA(encryptedKey, privateKeyPem);

            // 2. 用 AES 密钥解密 Body
            return decryptWithAES(encryptedBody, aesKeyBytes);

        } catch (Exception e) {
            log.error("Body decryption failed", e);
            throw new RuntimeException("Body decryption failed: " + e.getMessage(), e);
        }
    }

    /**
     * RSA 解密 AES 密钥
     */
    private static byte[] decryptWithRSA(String encryptedKeyBase64, String privateKeyPem) throws Exception {
        // Base64 解码
        byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64);

        // 导入 RSA 私钥
        PrivateKey privateKey = loadPrivateKey(privateKeyPem);

        // 解密
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedKey);
    }

    /**
     * AES-GCM 解密 Body
     */
    private static String decryptWithAES(String encryptedText, byte[] aesKeyBytes) throws Exception {
        // 解析格式: Base64(IV).Base64(Ciphertext)
        String[] parts = encryptedText.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encrypted body format");
        }

        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] ciphertext = Base64.getDecoder().decode(parts[1]);

        // 创建 AES 密钥
        SecretKeySpec secretKey = new SecretKeySpec(aesKeyBytes, "AES");

        // 解密
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);

        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 加载 RSA 私钥
     */
    private static PrivateKey loadPrivateKey(String privateKeyPem) throws Exception {
        // 移除 PEM 格式的头尾和换行
        String privateKeyContent = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        // Base64 解码
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);

        // 生成私钥
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
