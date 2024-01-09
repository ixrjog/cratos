package com.baiyi.cratos.encryptor;

import com.baiyi.cratos.annotation.DomainEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 10:58
 * @Version 1.0
 */
@Slf4j
@Component
public class EncryptDomainTest {

    @DomainEncrypt
    public void doEncrypt(EncryptorTest.EncryptDomain encryptDomain) {
        log.info("In progress encryptDomain: {}", encryptDomain);
    }

}
