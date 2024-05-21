package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.BusinessAssetBindService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/2/26 16:52
 * &#064;Version  1.0
 */
@Component
@RequiredArgsConstructor
public class SimpleEdsFacadeImpl implements SimpleEdsFacade {

    private final EdsAssetService edsAssetService;

    private final EdsAssetIndexService edsAssetIndexService;

    private final BusinessAssetBindService businessAssetBindService;

    @Override
    public void deleteEdsAssetById(Integer id) {
        if (edsAssetService.getById(id) == null) {
            return;
        }
        // 删除索引
        List<EdsAssetIndex> assetIndices = edsAssetIndexService.queryIndexByAssetId(id);
        if (!CollectionUtils.isEmpty(assetIndices)) {
            assetIndices.forEach(e -> edsAssetIndexService.deleteById(e.getId()));
        }
        // 删除绑定关系
        List<BusinessAssetBind> assetBinds = businessAssetBindService.queryByAssetId(id);
        if (!CollectionUtils.isEmpty(assetBinds)) {
            assetBinds.forEach(e -> businessAssetBindService.deleteById(e.getId()));
        }
        // 删除资产
        edsAssetService.deleteById(id);
    }

}
