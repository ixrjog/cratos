package com.baiyi.cratos.domain.util;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:57
 * &#064;Version 1.0
 */
public class JSONUtil {

    public static String writeValueAsString(Object object) {
        try {
            JSONMapper mapper = new JSONMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "";
        }
    }

}
