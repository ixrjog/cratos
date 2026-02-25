package com.baiyi.cratos.eds.acme;

import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:28
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AcmeDNSResolverFactory {

    private static final Map<EdsInstanceTypeEnum, AcmeDNSResolver> CONTEXT = new ConcurrentHashMap<>();

    public static void register(AcmeDNSResolver resolverBean) {
        CONTEXT.put(EdsInstanceTypeEnum.valueOf(resolverBean.getInstanceType()), resolverBean);
    }

    public static AcmeDNSResolver getAcmeDNSResolver(EdsInstanceTypeEnum instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static AcmeDNSResolver getAcmeDNSResolver(String instanceType) {
        return CONTEXT.get(EdsInstanceTypeEnum.valueOf(instanceType));
    }

}
