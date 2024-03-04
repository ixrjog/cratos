package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.facade.helper.EdsInstanceProviderDelegateHelper;
import com.baiyi.cratos.service.EdsInstanceService;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2024/3/4 10:24
 * @Version 1.0
 */
public class BaseEdsTest<C extends EdsAliyunConfigModel> extends BaseUnit {

    @Resource
    private EdsInstanceProviderDelegateHelper delegateHelper;

    @Resource
    private EdsInstanceService edsInstanceService;

    public void importInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        EdsInstanceProviderDelegate<?, ?> edsInstanceProviderDelegate = delegateHelper.buildDelegate(importInstanceAsset.getInstanceId(), importInstanceAsset.getAssetType());
        edsInstanceProviderDelegate.importAssets();
    }

    public C dddd(int instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsInstanceProviderDelegate<?, ?> edsInstanceProviderDelegate = delegateHelper.buildDelegate(instanceId, assetType);



        return (C) edsInstanceProviderDelegate.getInstance().getEdsConfigModel();
    }

}
