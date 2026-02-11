package com.baiyi.cratos.common.util;

import com.baiyi.cratos.BaseUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SSH 指纹计算工具测试
 */
class SshFingerprintUtilsTest extends BaseUnit {

    @Test
    void testCalcFingerprint_WithRsaKey() {
        String publicKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDTest user@host";
        String result = SshFingerprintUtils.calcFingerprint(null, publicKey);
        assertNotEquals("-", result);
        System.out.println(result);
    }

//    @Test
//    void testCalcFingerprint_WithCommentBeforeKey() {
//        // 公钥前面有注释文本
//        String publicKey = "This is a comment ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDTest";
//        String result = SshFingerprintUtils.calcFingerprint(null, publicKey);
//        assertNotEquals("-", result);
//        System.out.println(result);
//    }

    @Test
    void testCalcFingerprint_WithEd25519Key() {
        String publicKey = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAITest user@host";
        String result = SshFingerprintUtils.calcFingerprint(null, publicKey);
        assertNotEquals("-", result);
        System.out.println(result);
    }

    @Test
    void testCalcFingerprint_WithEcdsaKey() {
        String publicKey = "ecdsa-sha2-nistp256 AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAITest";
        String result = SshFingerprintUtils.calcFingerprint(null, publicKey);
        assertNotEquals("-", result);
        System.out.println(result);
    }

    @Test
    void testCalcFingerprint_WithDssKey() {
        String publicKey = "ssh-dss AAAAB3NzaC1kc3MAAACBATest user@host";
        String result = SshFingerprintUtils.calcFingerprint(null, publicKey);
        assertNotEquals("-", result);
        System.out.println(result);
    }

    @Test
    void testCalcFingerprint_WithEmptyPublicKey() {
        String result = SshFingerprintUtils.calcFingerprint(null, "");
        assertEquals("-", result);
        System.out.println(result);
    }

    @Test
    void testCalcFingerprint_WithNullPublicKey() {
        String result = SshFingerprintUtils.calcFingerprint(null, null);
        assertEquals("-", result);
        System.out.println(result);
    }

    @Test
    void testCalcFingerprint_WithInvalidKey() {
        String publicKey = "invalid-key-format";
        assertThrows(Exception.class, () -> {
            SshFingerprintUtils.calcFingerprint(null, publicKey);
        });
    }

}
