package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/30 14:19
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class NetworkUtil {

    /**
     * 是否在子网内
     *
     * @param network 192.168.5.100
     * @param mask    192.168.0.0/16
     * @return
     */
    public static boolean includeMask(String network, String mask) {
        if ("0.0.0.0/0".equals(mask) || "0.0.0.0".equals(mask)) {
            return true;
        }
        String[] networkIps = network.split("\\.");
        int ipAddr = (Integer.parseInt(networkIps[0]) << 24) | (Integer.parseInt(
                networkIps[1]) << 16) | (Integer.parseInt(networkIps[2]) << 8) | Integer.parseInt(networkIps[3]);
        int type = Integer.parseInt(mask.replaceAll(".*/", ""));
        int mask1 = 0xFFFFFFFF << (32 - type);
        String maskIp = mask.replaceAll("/.*", "");
        String[] maskIps = maskIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(maskIps[0]) << 24) | (Integer.parseInt(
                maskIps[1]) << 16) | (Integer.parseInt(maskIps[2]) << 8) | Integer.parseInt(maskIps[3]);
        return (ipAddr & mask1) == (cidrIpAddr & mask1);
    }

}
