package com.baiyi.cratos.domain.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:57
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class JSONUtil {

    public static String writeValueAsString(Object object) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "";
        }
    }

}
