package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/16 10:07
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class TokenGenerator {

    public static String generateToken() {
        UUID uuid = UUID.randomUUID();
        return Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
    }

}
