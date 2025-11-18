package com.baiyi.cratos.eds.business.processor;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/17 16:36
 * &#064;Version 1.0
 */
public interface BasePostImportAssetProcessor {

    BusinessTypeEnum getBusinessType();

    EdsAssetTypeEnum fromAssetType();

    void process(Integer businessId, EdsAsset asset);

}
