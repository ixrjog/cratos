package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import org.springframework.aop.support.AopUtils;

/**
 * @Author baiyi
 * @Date 2024/3/12 18:00
 * @Version 1.0
 */
public interface IEdsInstanceTypeAnnotate extends IAssetTypeAnnotate {

    default String getInstanceType() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .instanceType()
                .name();
    }

}
