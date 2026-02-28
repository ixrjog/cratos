package com.baiyi.cratos.eds.core.config.base;

import com.baiyi.cratos.eds.core.config.model.common.EdsCommonConfigModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 18:27
 * &#064;Version 1.0
 */
public interface HasAcme {

    EdsCommonConfigModel.ACME getAcme();

    void setAcme(EdsCommonConfigModel.ACME acme);

}
