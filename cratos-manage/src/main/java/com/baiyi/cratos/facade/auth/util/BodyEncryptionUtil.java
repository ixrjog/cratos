package com.baiyi.cratos.facade.auth.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Body 加密工具（用于响应加密）
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BodyEncryptionUtil {

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    /**
     * 用 AES-GCM 加密响应体
     * @param plaintext 明文响应
     * @param aesKeyBytes AES 密钥字节
     * @return 格式: Base64(IV).Base64(Ciphertext)
     */
    public static String encryptResponse(String plaintext, byte[] aesKeyBytes) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            SecretKeySpec secretKey = new SecretKeySpec(aesKeyBytes, "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);

            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            String ivBase64 = Base64.getEncoder().encodeToString(iv);
            String ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertext);

            return ivBase64 + "." + ciphertextBase64;
        } catch (Exception e) {
            throw new RuntimeException("Response encryption failed", e);
        }
    }

}
