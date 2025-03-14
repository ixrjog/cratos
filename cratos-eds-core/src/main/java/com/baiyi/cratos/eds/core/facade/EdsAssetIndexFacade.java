package com.baiyi.cratos.eds.core.facade;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/28 13:44
 * @Version 1.0
 */
public interface EdsAssetIndexFacade {

    /**
     * 保存资产索引，删除不存在的索引
     *
     * @param assetId
     * @param edsAssetIndexList
     */
    void saveAssetIndexList(int assetId, List<EdsAssetIndex> edsAssetIndexList);

    List<EdsAssetIndex> queryAssetIndexById(int assetId);

    List<EdsAssetIndex> queryAssetIndexByValue(String value);

    void deleteIndicesOfAsset(int assetId);

}
