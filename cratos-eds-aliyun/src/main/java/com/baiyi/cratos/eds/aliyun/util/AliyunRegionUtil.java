package com.baiyi.cratos.eds.aliyun.util;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/10 10:05
 * &#064;Version 1.0
 */
public class AliyunRegionUtil {

    /**
     * @param endpoint "alb.cn-hangzhou.aliyuncs.com"
     * @return cn-hangzhou
     */
    public static String toRegionId(String endpoint) {
        try {
            String[] s = endpoint.split("\\.");
            return s[s.length - 3];
        } catch (Exception e) {
            return endpoint;
        }
    }

}