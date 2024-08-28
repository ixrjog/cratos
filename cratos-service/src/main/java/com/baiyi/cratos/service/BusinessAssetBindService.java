package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.mapper.BusinessAssetBindMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/12 09:47
 * @Version 1.0
 */
public interface BusinessAssetBindService extends BaseUniqueKeyService<BusinessAssetBind, BusinessAssetBindMapper> {

    /**
     * 解除业务对象和资产的绑定关系
     *
     * @param business
     */
    void deleteByBusiness(BaseBusiness.HasBusiness business);

    List<BusinessAssetBind> queryByAssetId(int assetId);

}
