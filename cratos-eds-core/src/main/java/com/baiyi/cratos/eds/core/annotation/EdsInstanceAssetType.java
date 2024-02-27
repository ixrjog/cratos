package com.baiyi.cratos.eds.core.annotation;

import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/2/26 13:49
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EdsInstanceAssetType {

    EdsInstanceTypeEnum instanceType();

    EdsAssetTypeEnum assetType();

}
