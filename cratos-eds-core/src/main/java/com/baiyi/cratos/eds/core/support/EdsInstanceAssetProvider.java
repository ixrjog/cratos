package com.baiyi.cratos.eds.core.support;


import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.EdsAssetTypeOfAnnotate;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;

/**
 * @Author baiyi
 * @Date 2024/2/23 18:16
 * @Version 1.0
 */
public interface EdsInstanceAssetProvider<Config extends IEdsConfigModel, Asset> extends EdsAssetTypeOfAnnotate {

    void importAssets(ExternalDataSourceInstance<Config> instance);

    /**
     * Import a asset
     * @param instance
     * @param asset
     * @return
     */
    EdsAsset importAsset(ExternalDataSourceInstance<Config> instance, Asset asset);

    Config produceConfig(EdsConfig edsConfig);

    Asset assetLoadAs(String originalModel);

    void setConfig(EdsConfig edsConfig);

    Asset getAsset(EdsAsset asset);

}
