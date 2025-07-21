package com.baiyi.cratos.common.util;

import com.baiyi.cratos.domain.view.network.NetworkInfo;

/**
 * IP 网段计算工具类
 * 用于计算和分析 CIDR 格式的 IP 网段信息
 *
 * @author baiyi
 */
public class IPNetworkCalculator {

    public static NetworkInfo calculateNetworkInfo(String cidr) {
        if (cidr == null || cidr.trim()
                .isEmpty()) {
            throw new IllegalArgumentException("CIDR cannot be null or empty");
        }

        String[] parts = cidr.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CIDR format: " + cidr);
        }

        String ipAddress = parts[0].trim();
        int prefixLength;

        try {
            prefixLength = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid prefix length in CIDR: " + parts[1]);
        }

        if (prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("Prefix length must be between 0 and 32, got: " + prefixLength);
        }

        // 将 IP 地址转换为长整型
        long ipLong = ipToLong(ipAddress);

        // 计算子网掩码
        long subnetMask = (0xFFFFFFFFL << (32 - prefixLength)) & 0xFFFFFFFFL;

        // 计算网络地址
        long networkAddress = ipLong & subnetMask;

        // 计算广播地址
        long broadcastAddress = networkAddress | (0xFFFFFFFFL >> prefixLength);

        // 获取第一个和最后一个可用 IP
        String firstUsableIp = getFirstUsableIP(networkAddress, prefixLength);
        String lastUsableIp = getLastUsableIP(broadcastAddress, prefixLength);

        // 构建 IP 范围字符串
        String ipRange = firstUsableIp + " - " + lastUsableIp;

        // 创建并返回 NetworkInfo 对象
        return NetworkInfo.builder()
                .cidr(cidr)
                .networkAddress(longToIp(networkAddress))
                .subnetMask(longToIp(subnetMask))
                .ipRange(ipRange)
                .broadcastAddress(longToIp(broadcastAddress))
                .build();
    }

    /**
     * 将 IP 地址字符串转换为长整型
     *
     * @param ipAddress IP 地址字符串，例如 "192.168.1.1"
     * @return 长整型表示的 IP 地址
     */
    private static long ipToLong(String ipAddress) {
        if (ipAddress == null || ipAddress.trim()
                .isEmpty()) {
            throw new IllegalArgumentException("IP address cannot be null or empty");
        }

        String[] octets = ipAddress.trim()
                .split("\\.");
        if (octets.length != 4) {
            throw new IllegalArgumentException("Invalid IP address format: " + ipAddress);
        }

        long result = 0;
        for (int i = 0; i < 4; i++) {
            int octet;
            try {
                octet = Integer.parseInt(octets[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid IP address octet: " + octets[i]);
            }

            if (octet < 0 || octet > 255) {
                throw new IllegalArgumentException("IP address octet must be between 0 and 255, got: " + octet);
            }
            result += ((long) octet) << (8 * (3 - i));
        }
        return result & 0xFFFFFFFFL;
    }

    /**
     * 将长整型转换为 IP 地址字符串
     *
     * @param ip 长整型表示的 IP 地址
     * @return IP 地址字符串
     */
    private static String longToIp(long ip) {
        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xFF, (ip >> 16) & 0xFF, (ip >> 8) & 0xFF, ip & 0xFF);
    }

    /**
     * 判断是否为私有 IP 地址
     *
     * @param ip 长整型表示的 IP 地址
     * @return true 如果是私有 IP，false 否则
     */
    private static boolean isPrivateIP(long ip) {
        // 10.0.0.0/8: 10.0.0.0 - 10.255.255.255
        // 172.16.0.0/12: 172.16.0.0 - 172.31.255.255
        // 192.168.0.0/16: 192.168.0.0 - 192.168.255.255

        long class_A_start = ipToLong("10.0.0.0");
        long class_A_end = ipToLong("10.255.255.255");

        long class_B_start = ipToLong("172.16.0.0");
        long class_B_end = ipToLong("172.31.255.255");

        long class_C_start = ipToLong("192.168.0.0");
        long class_C_end = ipToLong("192.168.255.255");

        return (ip >= class_A_start && ip <= class_A_end) || (ip >= class_B_start && ip <= class_B_end) || (ip >= class_C_start && ip <= class_C_end);
    }

    /**
     * 获取第一个可用 IP 地址
     *
     * @param networkAddress 网络地址
     * @param prefixLength   前缀长度
     * @return 第一个可用 IP 地址字符串
     */
    private static String getFirstUsableIP(long networkAddress, int prefixLength) {
        if (prefixLength == 32) {
            // 主机路由，网络地址就是可用地址
            return longToIp(networkAddress);
        } else if (prefixLength == 31) {
            // 点对点链路，网络地址是第一个可用地址
            return longToIp(networkAddress);
        } else {
            // 常规网络，跳过网络地址
            return longToIp(networkAddress + 1);
        }
    }

    /**
     * 获取最后一个可用 IP 地址
     *
     * @param broadcastAddress 广播地址
     * @param prefixLength     前缀长度
     * @return 最后一个可用 IP 地址字符串
     */
    private static String getLastUsableIP(long broadcastAddress, int prefixLength) {
        if (prefixLength == 32) {
            // 主机路由，广播地址就是可用地址
            return longToIp(broadcastAddress);
        } else if (prefixLength == 31) {
            // 点对点链路，广播地址是最后一个可用地址
            return longToIp(broadcastAddress);
        } else {
            // 常规网络，跳过广播地址
            return longToIp(broadcastAddress - 1);
        }
    }

}
