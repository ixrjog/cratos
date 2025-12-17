package com.baiyi.cratos.eds.update;

import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午11:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateBusinessFromAssetHandlerImpl implements UpdateBusinessFromAssetHandler {

    private final BusinessAssetBoundService businessAssetBoundService;

    @Override
    public void update(EdsAsset asset) {
        List<BusinessAssetBound> businessAssetBounds = businessAssetBoundService.queryByAssetId(asset.getId());
        if (CollectionUtils.isEmpty(businessAssetBounds)) {
            return;
        }
        businessAssetBounds.forEach(e -> update(asset, e));
    }

    private void update(EdsAsset asset, BusinessAssetBound businessAssetBound) {
        IUpdateBusinessFromAssetProcessor provider = UpdateBusinessFromAssetProcessorFactory.getProvider(
                businessAssetBound.getBusinessType());
        if (provider != null) {
            provider.update(asset, businessAssetBound);
        }
    }

}
