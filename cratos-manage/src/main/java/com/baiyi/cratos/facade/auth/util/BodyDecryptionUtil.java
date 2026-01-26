package com.baiyi.cratos.facade.auth.util;

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
 * Body 解密工具
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
     */
    public static String decryptBody(String encryptedBody, String encryptedKey, String privateKeyPem) {
        try {
            byte[] aesKeyBytes = decryptWithRSA(encryptedKey, privateKeyPem);
            return decryptWithAES(encryptedBody, aesKeyBytes);
        } catch (Exception e) {
            log.error("Body decryption failed", e);
            throw new RuntimeException("Body decryption failed: " + e.getMessage(), e);
        }
    }

    private static byte[] decryptWithRSA(String encryptedKeyBase64, String privateKeyPem) throws Exception {
        byte[] encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64);
        PrivateKey privateKey = loadPrivateKey(privateKeyPem);
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedKey);
    }

    private static String decryptWithAES(String encryptedText, byte[] aesKeyBytes) throws Exception {
        String[] parts = encryptedText.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encrypted body format");
        }
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] ciphertext = Base64.getDecoder().decode(parts[1]);
        SecretKeySpec secretKey = new SecretKeySpec(aesKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static PrivateKey loadPrivateKey(String privateKeyPem) throws Exception {
        String privateKeyContent = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
