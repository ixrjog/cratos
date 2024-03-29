package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.mapper.EdsAssetIndexMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/27 14:33
 * @Version 1.0
 */
public interface EdsAssetIndexService extends BaseUniqueKeyService<EdsAssetIndex>, BaseService<EdsAssetIndex, EdsAssetIndexMapper> {

    List<EdsAssetIndex> queryIndexByAssetId(int assetId);

    int selectCountByAssetId(int assetId);

    List<EdsAssetIndex> queryIndexByValue(String value);

}

