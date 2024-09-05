package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/5 17:24
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class NetworkUtil {

    /**
     * 判断子网是否在Network中
     *
     * @param network
     * @param subnet
     * @return
     */
    public static boolean inNetwork(String network, String subnet) {
        final String subnetIp = StringUtils.substringBefore(subnet, "/");
        final String subnetMask = StringUtils.substringAfter(subnet, "/");
        final String subnetBeginIp = IpUtil.getBeginIpStr(subnetIp, subnetMask);
        final String subnetEndIp = IpUtil.getEndIpStr(subnetIp, subnetMask);
        return IpUtil.isInRange(subnetBeginIp, network) && IpUtil.isInRange(subnetEndIp, network);
    }

}
