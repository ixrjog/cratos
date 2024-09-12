package com.baiyi.cratos.eds.core.update;

import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.service.BusinessAssetBindService;
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
public class UpdateBusinessFromAssetHandler {

    private final BusinessAssetBindService businessAssetBindService;

    public void update(EdsAsset asset) {
        List<BusinessAssetBind> businessAssetBinds = businessAssetBindService.queryByAssetId(asset.getId());
        if (CollectionUtils.isEmpty(businessAssetBinds)) {
            return;
        }
        businessAssetBinds.forEach(e -> update(asset, e));
    }

    private void update(EdsAsset asset, BusinessAssetBind businessAssetBind) {
        IUpdateBusinessFromAssetProcessor provider = UpdateBusinessFromAssetProcessorFactory.getProvider(
                businessAssetBind.getBusinessType());
        if (provider != null) {
            provider.update(asset, businessAssetBind);
        }
    }

}
