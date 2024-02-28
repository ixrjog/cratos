package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.facade.helper.EdsInstanceProviderDelegateHelper;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/28 15:14
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsAssetWrapper extends BaseDataTableConverter<EdsAssetVO.Asset, EdsAsset> implements IBaseWrapper<EdsAssetVO.Asset> {

    private final EdsInstanceProviderDelegateHelper delegateHelper;

    @Override
    public void wrap(EdsAssetVO.Asset asset) {
        EdsInstanceProviderDelegate<?, ?> edsInstanceProviderDelegate = delegateHelper.buildDelegate(asset.getInstanceId(), asset.getAssetType());
        // TODO 是否要序列化对象？
        asset.setOriginalAsset(edsInstanceProviderDelegate.getProvider()
                .assetLoadAs(asset.getOriginalModel()));
    }

}
