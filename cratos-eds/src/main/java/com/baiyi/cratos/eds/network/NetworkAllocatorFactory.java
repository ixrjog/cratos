package com.baiyi.cratos.eds.network;

import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/9 16:09
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class NetworkAllocatorFactory {

    private static final Map<EdsInstanceTypeEnum, NetworkAllocator> CONTEXT = new ConcurrentHashMap<>();

    public static void register(NetworkAllocator networkAllocator) {
        CONTEXT.put(EdsInstanceTypeEnum.valueOf(networkAllocator.getInstanceType()), networkAllocator);
    }

    public static NetworkAllocator getNetworkAllocator(EdsInstanceTypeEnum instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static NetworkAllocator getNetworkAllocator(String instanceType) {
        return CONTEXT.get(EdsInstanceTypeEnum.valueOf(instanceType));
    }

}