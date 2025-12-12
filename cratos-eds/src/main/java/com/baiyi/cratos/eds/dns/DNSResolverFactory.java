package com.baiyi.cratos.eds.dns;

import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 16:04
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class DNSResolverFactory {

    private static final Map<EdsInstanceTypeEnum, DNSResolver> CONTEXT = new ConcurrentHashMap<>();

    public static void register(DNSResolver resolverBean) {
        CONTEXT.put(EdsInstanceTypeEnum.valueOf(resolverBean.getInstanceType()), resolverBean);
    }

    public static DNSResolver getDNSResolver(EdsInstanceTypeEnum instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static DNSResolver getDNSResolver(String instanceType) {
        return CONTEXT.get(EdsInstanceTypeEnum.valueOf(instanceType));
    }

}
