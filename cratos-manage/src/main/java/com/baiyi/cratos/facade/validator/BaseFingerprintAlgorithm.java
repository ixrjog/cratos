package com.baiyi.cratos.facade.validator;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.domain.generator.Credential;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2024/2/4 13:09
 * @Version 1.0
 */
public abstract class BaseFingerprintAlgorithm {

    public void calcAndFillInFingerprint(Credential credential) {
        String fingerprint = calcFingerprint(credential.getCredential(), credential.getCredential2());
        credential.setFingerprint(fingerprint);
    }

    abstract public CredentialTypeEnum getType();

    protected String calcFingerprint(String privateKey, String publicKey) {
        String fingerprint = "-";
        // ssh-ed25519
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

    protected String calcFingerprint(String publicKey) {
        return calcFingerprint(null, publicKey);
    }

}
