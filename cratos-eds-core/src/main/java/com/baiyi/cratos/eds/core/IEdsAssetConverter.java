package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;

/**
 * @Author baiyi
 * @Date 2024/2/29 10:44
 * @Version 1.0
 */
public interface IEdsAssetConverter<Config extends HasEdsConfig, Asset> {

    EdsAsset toEdsAsset(ExternalDataSourceInstance<Config> instance, Asset entity);

}
