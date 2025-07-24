package com.baiyi.cratos.my;


import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.api.client.util.Sets;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/24 10:42
 * &#064;Version 1.0
 */
public class AssetTest extends BaseUnit {

    @Resource
    private EdsAssetIndexService edsAssetIndexService;

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test() {
        Set<Integer> assetIdSet = Sets.newHashSet();
        Set<Integer> notAssetIdSet = Sets.newHashSet();
        List<EdsAssetIndex> indices = edsAssetIndexService.queryInvalidIndex();
        for (EdsAssetIndex index : indices) {
            boolean exist = isAssetExist(index.getAssetId(), assetIdSet, notAssetIdSet);
            if (!exist) {
                System.out.println(
                        "Asset not exist: " + index.getAssetId() + ", " + index.getName() + ", " + index.getValue());
                edsAssetIndexService.deleteById(index.getId());
            }
        }
    }

    private boolean isAssetExist(int assetId, Set<Integer> assetIdSet, Set<Integer> notAssetIdSet) {
        if (assetIdSet.contains(assetId)) {
            return true;
        }
        if (notAssetIdSet.contains(assetId)) {
            return false;
        }
        EdsAsset asset = edsAssetService.getById(assetId);
        if (asset == null) {
            notAssetIdSet.add(assetId);
            return false;
        } else {
            assetIdSet.add(assetId);
            return true;
        }
    }

}
