package com.baiyi.cratos.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午2:55
 * &#064;Version 1.0
 */
public class CratosHostHolder {

    private static final CratosHost host = newBuild();

    public static CratosHost get() {
        return host;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CratosHost {

        @Builder.Default
        private String hostAddress = "127.0.0.1";

        @Builder.Default
        private String hostname = "opscloud";
    }

    private static CratosHost newBuild() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            return CratosHost.builder()
                    .hostAddress(addr.getHostAddress())
                    .hostname(hostname)
                    .build();
        } catch (UnknownHostException ex) {
            return CratosHost.builder()
                    .build();
        }
    }

}
