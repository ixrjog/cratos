package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.param.asset.AssetMaturityParam;
import com.baiyi.cratos.mapper.AssetMaturityMapper;
import com.baiyi.cratos.service.base.BaseQueryByExpiryService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午1:42
 * &#064;Version 1.0
 */
public interface AssetMaturityService extends BaseUniqueKeyService<AssetMaturity>, BaseValidService<AssetMaturity, AssetMaturityMapper>, SupportBusinessService, BaseQueryByExpiryService<AssetMaturity> {

    DataTable<AssetMaturity> queryAssetMaturityPage(AssetMaturityParam.AssetMaturityPageQueryParam pageQuery);

}
