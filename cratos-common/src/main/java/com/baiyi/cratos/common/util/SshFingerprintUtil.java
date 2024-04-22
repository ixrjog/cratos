package com.baiyi.cratos.common.util;

import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2024/4/19 下午5:38
 * @Version 1.0
 */
public class SshFingerprintUtil {

    private SshFingerprintUtil() {
    }

    public static String calcFingerprint(String privateKey, String publicKey) {
        String fingerprint = "-";
        if (!org.springframework.util.StringUtils.hasText(publicKey)) {
            return fingerprint;
        }
        if (publicKey.contains("ssh-")) {
            publicKey = publicKey.substring(publicKey.indexOf("ssh-"));
        } else if (publicKey.contains("ecdsa-")) {
            publicKey = publicKey.substring(publicKey.indexOf("ecdsa-"));
        }
        try {
            KeyPair keyPair = KeyPair.load(new JSch(), StringUtils.hasText(privateKey) ? privateKey.getBytes() : null, publicKey.getBytes());
            if (keyPair == null) {
                throw new InvalidCredentialException("The SSH key(pair) incorrect.");
            }
            fingerprint = keyPair.getFingerPrint();
        } catch (JSchException e) {
            throw new InvalidCredentialException("The SSH key(pair) incorrect err: {}", e.getMessage());
        }
        return fingerprint;
    }

}
