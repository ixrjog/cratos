package com.baiyi.cratos.processor;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/17 16:36
 * &#064;Version 1.0
 */
public interface BasePostImportAssetProcessor extends InitializingBean {

    BusinessTypeEnum getBusinessType();

    EdsAssetTypeEnum fromAssetType();

    void process(Integer businessId, EdsAsset asset, Map<String, Object> context);

    default void afterPropertiesSet() {
        PostImportAssetProcessorFactory.register(this);
    }

}
