package com.baiyi.cratos.eds.support;

import com.baiyi.cratos.eds.IExternalDataSourceInstance;

/**
 * @Author baiyi
 * @Date 2024/2/23 18:16
 * @Version 1.0
 */
public interface EdsInstanceProvider<I extends IExternalDataSourceInstance, A> {

    void importAssets(I instance);

    void pushAsset(I instance, A asset);

    String getAssetType();

}
