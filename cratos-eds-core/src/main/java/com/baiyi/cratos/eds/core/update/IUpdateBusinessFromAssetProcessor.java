package com.baiyi.cratos.eds.core.update;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAsset;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午11:24
 * &#064;Version 1.0
 */
public interface IUpdateBusinessFromAssetProcessor extends BaseBusiness.IBusinessTypeAnnotate, InitializingBean {

    void update(EdsAsset asset, BusinessAssetBind businessAssetBind);

}
