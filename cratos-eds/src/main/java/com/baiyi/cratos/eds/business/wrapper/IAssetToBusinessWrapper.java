package com.baiyi.cratos.eds.business.wrapper;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.EdsAssetTypeOfAnnotate;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:17
 * @Version 1.0
 */
public interface IAssetToBusinessWrapper<B extends ToBusinessTarget> extends BaseBusiness.IBusinessTypeAnnotate, EdsAssetTypeOfAnnotate, InitializingBean {

    void wrap(EdsAssetVO.Asset asset);

    EdsAssetVO.AssetToBusiness<B> getAssetToBusiness(EdsAssetVO.Asset asset);

    default void afterPropertiesSet() {
        AssetToBusinessWrapperFactory.register(this);
    }

}
