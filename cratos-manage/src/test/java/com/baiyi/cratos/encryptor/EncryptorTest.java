package com.baiyi.cratos.encryptor;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.annotation.FieldEncrypt;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/8 10:45
 * @Version 1.0
 */
@Slf4j
public class EncryptorTest extends BaseUnit {

    @Resource
    private EncryptDomainTest encryptDomainTest;

    @Resource
    private StringEncryptor stringEncryptor;

    @Data
    @Builder
    @FieldSensitive
    @EncryptedDomain
    public static class EncryptDomain {

        @FieldEncrypt(erase = false)
        @Builder.Default
        private String password = "000000000";

        @FieldEncrypt()
        @Builder.Default
        private String erasePassword = "11111111111";

    }

    @Test
    void encryptTest() {
        EncryptDomain encryptDomain = EncryptDomain.builder().build();
        encryptDomainTest.doEncrypt(encryptDomain);
        log.info("After execution encryptDomain: {}", encryptDomain);
    }

    @Test
    void encryptPasswordTest() {
        log.error(stringEncryptor.encrypt(""));
    }

    @Test
    void decryptTest(){
        log.error(stringEncryptor.decrypt(""));
    }

}
