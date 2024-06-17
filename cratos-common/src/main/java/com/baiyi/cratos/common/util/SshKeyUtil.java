package com.baiyi.cratos.common.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 15:02
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public final class SshKeyUtil {

    /**
     * https://github.com/bastillion-io/Bastillion/blob/master/src/main/java/io/bastillion/manage/util/SSHUtil.java
     * returns public key fingerprint
     *
     * @param publicKey public key
     * @return fingerprint of public key
     */
    public static String calcFingerprint(String publicKey) {
        String fingerprint = "-";
        if (StringUtils.isNotEmpty(publicKey)) {
            if (publicKey.contains("ssh-")) {
                publicKey = publicKey.substring(publicKey.indexOf("ssh-"));
            } else if (publicKey.contains("ecdsa-")) {
                publicKey = publicKey.substring(publicKey.indexOf("ecdsa-"));
            }
            try {
                KeyPair keyPair = KeyPair.load(new JSch(), null, publicKey.getBytes());
                if (keyPair != null) {
                    fingerprint = keyPair.getFingerPrint();
                }
            } catch (JSchException ignored) {
            }
        }
        return fingerprint;
    }

    /**
     * returns public key type
     *
     * @param publicKey public key
     * @return fingerprint of public key
     */
    public static String getKeyType(String publicKey) {
        String keyType = null;
        if (StringUtils.isNotEmpty(publicKey)) {
            if (publicKey.contains("ssh-")) {
                publicKey = publicKey.substring(publicKey.indexOf("ssh-"));
            } else if (publicKey.contains("ecdsa-")) {
                publicKey = publicKey.substring(publicKey.indexOf("ecdsa-"));
            }
            try {
                KeyPair keyPair = KeyPair.load(new JSch(), null, publicKey.getBytes());
                if (keyPair != null) {
                    int type = keyPair.getKeyType();
                    if (KeyPair.DSA == type) {
                        keyType = "DSA";
                    } else if (KeyPair.RSA == type) {
                        keyType = "RSA";
                    } else if (KeyPair.ECDSA == type) {
                        keyType = "ECDSA";
                    } else if (KeyPair.ED25519 == type) {
                        keyType = "ED25519";
                    } else if (KeyPair.UNKNOWN == type) {
                        keyType = "UNKNOWN";
                    } else if (KeyPair.ERROR == type) {
                        keyType = "ERROR";
                    }
                }
            } catch (JSchException ex) {
                log.error(ex.toString(), ex);
            }
        }
        return keyType;
    }

}
