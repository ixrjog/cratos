package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.param.asset.AssetMaturityParam;
import com.baiyi.cratos.domain.view.asset.AssetMaturityVO;
import com.baiyi.cratos.facade.AssetMaturityFacade;
import com.baiyi.cratos.service.AssetMaturityService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.AssetMaturityWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午2:20
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AssetMaturityFacadeImpl implements AssetMaturityFacade {

    private final AssetMaturityService assetMaturityService;
    private final AssetMaturityWrapper assetMaturityWrapper;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.ASSET_MATURITY)
    public DataTable<AssetMaturityVO.AssetMaturity> queryAssetMaturityPage(
            AssetMaturityParam.AssetMaturityPageQuery pageQuery) {
        DataTable<AssetMaturity> table = assetMaturityService.queryAssetMaturityPage(pageQuery.toParam());
        return assetMaturityWrapper.wrapToTarget(table);
    }

    @Override
    public void addAssetMaturity(AssetMaturityParam.AddAssetMaturity addAssetMaturity) {
        AssetMaturity assetMaturity = addAssetMaturity.toTarget();
        if (assetMaturityService.getByUniqueKey(assetMaturity) == null) {
            assetMaturityService.add(assetMaturity);
        }
    }

    @Override
    public void updateAssetMaturity(AssetMaturityParam.UpdateAssetMaturity updateAssetMaturity) {
        AssetMaturity assetMaturity = updateAssetMaturity.toTarget();
        assetMaturityService.updateByPrimaryKey(assetMaturity);
    }

    @Override
    public void deleteById(int id) {
        assetMaturityService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return assetMaturityService;
    }
}
