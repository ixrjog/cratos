package com.baiyi.cratos.facade;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.asset.AssetMaturityParam;
import com.baiyi.cratos.domain.view.asset.AssetMaturityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午2:19
 * &#064;Version 1.0
 */
public interface AssetMaturityFacade extends HasSetValid {

    DataTable<AssetMaturityVO.AssetMaturity> queryAssetMaturityPage(
            AssetMaturityParam.AssetMaturityPageQuery pageQuery);

    void addAssetMaturity(AssetMaturityParam.AddAssetMaturity addAssetMaturity);

    void updateAssetMaturity(AssetMaturityParam.UpdateAssetMaturity updateAssetMaturity);

    void deleteById(int id);

}
