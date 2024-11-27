package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsAssetMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.SupportBusinessService;
import lombok.NonNull;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/26 15:24
 * @Version 1.0
 */
public interface EdsAssetService extends BaseUniqueKeyService<EdsAsset, EdsAssetMapper>, SupportBusinessService {

    List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType);

    List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType, String region);

    DataTable<EdsAsset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery pageQuery);

    List<EdsAsset> queryAssetByParam(String assetKey, String assetType);

    void clear(@NonNull EdsAsset record);

}
