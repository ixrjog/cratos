package com.baiyi.cratos.eds.kubernetes.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/3/17 09:38
 * @Version 1.0
 */
@Getter
public enum KubernetesProvidersEnum {

    /**
     * 供应商
     */
    AMAZON_EKS("AmazonEKS"),
    DEFAULT("Default");

    private final String displayName;

    KubernetesProvidersEnum(String desc) {
        this.displayName = desc;
    }

}