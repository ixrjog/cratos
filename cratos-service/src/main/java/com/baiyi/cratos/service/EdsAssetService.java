package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsAssetMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/26 15:24
 * @Version 1.0
 */
public interface EdsAssetService extends BaseUniqueKeyService<EdsAsset>, BaseService<EdsAsset, EdsAssetMapper> {

    List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType);

    DataTable<EdsAsset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery pageQuery);

}
