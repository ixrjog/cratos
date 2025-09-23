package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.application.EdsArmsFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ARMS_APP_HOME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ENV;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/4 14:49
 * &#064;Version 1.0
 */
@Component
@AllArgsConstructor
public class EdsArmsFacadeImpl implements EdsArmsFacade {

    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'EDS:ALIYUN:ARMS:HOME:APP:' + #applicationName + ':NAMESPACE:' + #namespace", unless = "#result == null")
    public KubernetesVO.ARMS makeArms(@NotNull String applicationName, @NotNull String namespace) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(APP_NAME, applicationName);
        if (CollectionUtils.isEmpty(indices)) {
            return KubernetesVO.ARMS.NO_DATA;
        }
        for (EdsAssetIndex index : indices) {
            EdsAsset asset = edsAssetService.getById(index.getAssetId());
            if (asset == null || !EdsAssetTypeEnum.ALIYUN_ARMS_TRACE_APPS.name()
                    .equals(asset.getAssetType())) {
                continue;
            }
            EdsAssetIndex envIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(), ENV);
            if (envIndex == null || !namespace.equals(envIndex.getValue())) {
                continue;
            }
            EdsAssetIndex homeIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(), ALIYUN_ARMS_APP_HOME);
            if (homeIndex != null && homeIndex.getValue() != null) {
                return KubernetesVO.ARMS.builder()
                        .home(homeIndex.getValue())
                        .build();
            }
        }
        return KubernetesVO.ARMS.NO_DATA;
    }

}
