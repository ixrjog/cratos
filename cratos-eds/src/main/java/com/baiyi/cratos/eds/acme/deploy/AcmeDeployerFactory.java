package com.baiyi.cratos.eds.acme.deploy;

import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 10:02
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AcmeDeployerFactory {

    private static final Map<EdsInstanceTypeEnum, AcmeDeployer> CONTEXT = new ConcurrentHashMap<>();

    public static void register(AcmeDeployer acmeEdsDeployBean) {
        CONTEXT.put(EdsInstanceTypeEnum.valueOf(acmeEdsDeployBean.getInstanceType()), acmeEdsDeployBean);
    }

    public static AcmeDeployer getAcmeDeployer(EdsInstanceTypeEnum instanceType) {
        return CONTEXT.get(instanceType);
    }

    public static AcmeDeployer getAcmeDeployer(String instanceType) {
        return CONTEXT.get(EdsInstanceTypeEnum.valueOf(instanceType));
    }

}
