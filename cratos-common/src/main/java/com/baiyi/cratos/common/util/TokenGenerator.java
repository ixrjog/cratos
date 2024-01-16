package com.baiyi.cratos.common.util;

import java.util.UUID;

/**
 * @Author baiyi
 * @Date 2024/1/16 10:07
 * @Version 1.0
 */
public class TokenGenerator {

    private TokenGenerator() {
    }

    public static String generateToken() {
        UUID uuid = UUID.randomUUID();
        return Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
    }

}
