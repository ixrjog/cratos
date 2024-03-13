package com.baiyi.cratos.eds.core.support;


import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.IEdsInstanceTypeAnnotate;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;

/**
 * @Author baiyi
 * @Date 2024/2/23 18:16
 * @Version 1.0
 */
public interface EdsInstanceProvider<C extends IEdsConfigModel, A> extends IEdsInstanceTypeAnnotate {

    void importAssets(ExternalDataSourceInstance<C> instance);

    EdsAsset importAsset(ExternalDataSourceInstance<C> instance, A asset);

    C produceConfig(EdsConfig edsConfig);

    A assetLoadAs(String originalModel);

    void setConfig(EdsConfig edsConfig);

}
