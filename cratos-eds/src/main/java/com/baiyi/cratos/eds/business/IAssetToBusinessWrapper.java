package com.baiyi.cratos.eds.business;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.IToBusinessTarget;
import com.baiyi.cratos.eds.core.IEdsInstanceTypeAnnotate;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:17
 * @Version 1.0
 */
public interface IAssetToBusinessWrapper<B extends IToBusinessTarget> extends BaseBusiness.IBusinessTypeAnnotate, IEdsInstanceTypeAnnotate, InitializingBean {

    void wrap(EdsAssetVO.Asset asset);

    EdsAssetVO.AssetToBusiness<B> getToBusinessTarget(EdsAssetVO.Asset asset);

    default void afterPropertiesSet() {
        AssetToBusinessWrapperFactory.register(this);
    }

}
