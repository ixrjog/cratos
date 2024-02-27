package com.baiyi.cratos.eds.core.support;


import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
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

    EdsAsset pushAsset(ExternalDataSourceInstance<C> instance, A asset);

    C produce(EdsConfig edsConfig);

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
