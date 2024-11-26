package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import org.springframework.aop.support.AopUtils;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:21
 * @Version 1.0
 */
public interface EdsAssetTypeOfAnnotate extends EdsInstanceTypeOfAnnotate {

    // 从注解中获取
    default String getAssetType() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .assetTypeOf()
                .name();
    }

}
