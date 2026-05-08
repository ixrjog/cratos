package com.baiyi.cratos.eds.security.apirisk.test.signature;

import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 13:39
 * &#064;Version 1.0
 */
public class SignatureFactory {

    private static final Map<SignatureAlgorithmEnum, SignatureAlgorithm> CONTEXT = new ConcurrentHashMap<>();

    public static void register(SignatureAlgorithm signatureAlgorithmBean) {
        CONTEXT.put(signatureAlgorithmBean.getSignatureAlgorithmEnum(), signatureAlgorithmBean);
    }

    public static SignatureAlgorithm getSignatureAlgorithm(SignatureAlgorithmEnum signatureAlgorithmEnum) {
        return CONTEXT.get(signatureAlgorithmEnum);
    }

}
