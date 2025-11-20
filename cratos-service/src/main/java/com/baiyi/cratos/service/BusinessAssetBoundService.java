package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.mapper.BusinessAssetBoundMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/12 09:47
 * @Version 1.0
 */
public interface BusinessAssetBoundService extends BaseUniqueKeyService<BusinessAssetBound, BusinessAssetBoundMapper> {

    /**
     * 解除业务对象和资产的绑定关系
     *
     * @param business
     */
    void deleteByBusiness(BaseBusiness.HasBusiness business);

    List<BusinessAssetBound> queryByAssetId(int assetId);

    List<BusinessAssetBound> queryByBusiness(BaseBusiness.HasBusiness business);

    List<BusinessAssetBound> queryByBusiness(BaseBusiness.HasBusiness business, String assetType);

}
