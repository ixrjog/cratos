package com.baiyi.cratos.secutity;

import lombok.AllArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/15 13:51
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class CratosPasswordEncoder implements PasswordEncoder {

    private final StringEncryptor stringEncryptor;

    @Override
    public String encode(CharSequence rawPassword) {
        return stringEncryptor.encrypt(String.valueOf(rawPassword));
    }

    /**
     *
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后密码
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
       String pw = stringEncryptor.decrypt(encodedPassword);
       return rawPassword.equals(pw);
    }

}
