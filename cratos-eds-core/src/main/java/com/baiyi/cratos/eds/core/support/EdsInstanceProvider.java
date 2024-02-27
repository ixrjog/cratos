package com.baiyi.cratos.eds.core.support;


import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import org.springframework.aop.support.AopUtils;

/**
 * @Author baiyi
 * @Date 2024/2/23 18:16
 * @Version 1.0
 */
public interface EdsInstanceProvider<C extends IEdsConfigModel, A> {

    void importAssets(ExternalDataSourceInstance<C> instance);

    void pushAsset(ExternalDataSourceInstance<C> instance, A asset);

    default String getInstanceType() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .instanceType()
                .name();
    }

    // 从注解中获取
    default String getAssetType() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .assetType()
                .name();
    }

}
