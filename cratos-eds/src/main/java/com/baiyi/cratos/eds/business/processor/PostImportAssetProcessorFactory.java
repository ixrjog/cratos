package com.baiyi.cratos.eds.business.processor;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/17 16:33
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImportAssetProcessorFactory {

    private static final Map<BusinessTypeEnum, Map<EdsAssetTypeEnum, BasePostImportAssetProcessor>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BasePostImportAssetProcessor processorBean) {
        if (processorBean == null) {
            return;
        }
        BusinessTypeEnum businessType = processorBean.getBusinessType();
        EdsAssetTypeEnum fromAssetType = processorBean.fromAssetType();
        CONTEXT.computeIfAbsent(businessType, k -> new ConcurrentHashMap<>())
                .put(fromAssetType, processorBean);
        log.info("Registered PostImportAssetProcessor: businessType={}, assetType={}", businessType, fromAssetType);
    }

    public static BasePostImportAssetProcessor getProcessor(BusinessTypeEnum businessType, EdsAssetTypeEnum fromAssetType) {
        Map<EdsAssetTypeEnum, BasePostImportAssetProcessor> assetMap = CONTEXT.get(businessType);
        if (assetMap == null) {
            log.warn("No processors registered for businessType={}", businessType);
            return null;
        }
        BasePostImportAssetProcessor processor = assetMap.get(fromAssetType);
        if (processor == null) {
            log.warn("No processor found for businessType={}, fromAssetType={}", businessType, fromAssetType);
        }
        return processor;
    }

}
