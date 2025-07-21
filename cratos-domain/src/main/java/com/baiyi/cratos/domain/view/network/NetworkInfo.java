package com.baiyi.cratos.domain.view.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/21 10:21
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkInfo {

    /**
     * • **网络地址**: 10.244.0.0
     * • **子网掩码**: 255.254.0.0
     * • **IP 范围**: 10.244.0.0 - 10.245.255.255
     * • **广播地址**: 10.245.255.255
     */

    /**
     * > Basic Information:
     * • Network Address: 10.244.0.0
     * • Subnet Mask: 255.254.0.0
     * • IP Range: 10.244.0.0 - 10.245.255.255
     * • Broadcast Address: 10.245.255.255
     */

    private String cidr;
    private String networkAddress;
    private String subnetMask;
    private String ipRange;
    private String broadcastAddress;

    private String details;

}
