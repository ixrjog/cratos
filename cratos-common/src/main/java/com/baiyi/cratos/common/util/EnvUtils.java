package com.baiyi.cratos.common.util;

import com.baiyi.cratos.domain.generator.Env;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/4 10:44
 * &#064;Version 1.0
 */
public class EnvUtils {

    public static String getEnvSuffix(Map<String, Env> envMap, String name) {
        if (name == null || envMap == null || envMap.isEmpty()) {
            return null;
        }
        int lastIndex = name.lastIndexOf("-");
        if (lastIndex == -1 || lastIndex == name.length() - 1) {
            return envMap.containsKey(name) ? name : null;
        }
        String envName = name.substring(lastIndex + 1);
        return envMap.containsKey(envName) ? envName : null;
    }

}
