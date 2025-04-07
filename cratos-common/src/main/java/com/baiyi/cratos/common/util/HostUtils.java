package com.baiyi.cratos.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 10:55
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HostUtils {

    public static String getHostAddress() throws UnknownHostException {
        return InetAddress.getLocalHost()
                .getHostAddress();
    }

    public static InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static String getHostName() {
        try {
            return getInetAddress().getCanonicalHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

}
