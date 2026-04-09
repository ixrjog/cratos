package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/5 17:24
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class NetworkUtils {

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
        final String subnetBeginIp = IpUtils.getBeginIpStr(subnetIp, subnetMask);
        final String subnetEndIp = IpUtils.getEndIpStr(subnetIp, subnetMask);
        return IpUtils.isInRange(subnetBeginIp, network) && IpUtils.isInRange(subnetEndIp, network);
    }

    private static final Pattern IP_PATTERN = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)/" + "(3[0-2]|[1-2]?\\d)$");

    public static boolean isValidIpRange(String ipRange) {
        return IP_PATTERN.matcher(ipRange)
                .matches();
    }

    public static boolean isCidr(String cidr) {
        if (cidr == null || !cidr.contains("/")) {
            return false;
        }
        String[] parts = cidr.split("/");
        if (parts.length != 2) {
            return false;
        }
        try {
            int prefix = Integer.parseInt(parts[1]);
            if (prefix < 0 || prefix > 32) {
                return false;
            }
            InetAddress.getByName(parts[0]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static long[] cidrToRange(String cidr) {
        String[] parts = cidr.split("/");
        String[] ipParts = parts[0].split("\\.");
        long start = (Long.parseLong(ipParts[0]) << 24) | (Long.parseLong(ipParts[1]) << 16) | (Long.parseLong(
                ipParts[2]) << 8) | Long.parseLong(ipParts[3]);
        int prefix = Integer.parseInt(parts[1]);
        long end = start | ((1L << (32 - prefix)) - 1);
        return new long[]{start, end};
    }

    public static String longToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
    }

}
