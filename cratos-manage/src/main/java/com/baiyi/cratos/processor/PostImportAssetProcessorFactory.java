package com.baiyi.cratos.processor;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.List;
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

    private static final Map<BusinessTypeEnum, Map<EdsAssetTypeEnum, List<BasePostImportAssetProcessor>>> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BasePostImportAssetProcessor processorBean) {
        if (processorBean == null) {
            return;
        }
        BusinessTypeEnum businessType = processorBean.getBusinessType();
        EdsAssetTypeEnum fromAssetType = processorBean.fromAssetType();
        List<BasePostImportAssetProcessor> processors = CONTEXT.computeIfAbsent(
                        businessType, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(fromAssetType, k -> Lists.newArrayList());
        processors.add(processorBean);
        // 按@Order注解排序，数值小的优先
        processors.sort((p1, p2) -> {
            int order1 = getOrder(p1);
            int order2 = getOrder(p2);
            return Integer.compare(order1, order2);
        });
        log.info(
                "Registered PostImportAssetProcessor: businessType={}, assetType={}, order={}", businessType,
                fromAssetType, getOrder(processorBean)
        );
    }

    private static int getOrder(BasePostImportAssetProcessor processor) {
        Order order = AnnotationUtils.findAnnotation(processor.getClass(), Order.class);
        // 没有注解优先级最低
        return order != null ? order.value() : Integer.MAX_VALUE;
    }

    public static List<BasePostImportAssetProcessor> getProcessors(BusinessTypeEnum businessType,
                                                                   EdsAssetTypeEnum fromAssetType) {
        Map<EdsAssetTypeEnum, List<BasePostImportAssetProcessor>> assetMap = CONTEXT.get(businessType);
        if (assetMap == null) {
            log.warn("No processors registered for businessType={}", businessType);
            return Lists.newArrayList();
        }
        List<BasePostImportAssetProcessor> processors = assetMap.get(fromAssetType);
        return processors != null ? processors : Lists.newArrayList();
    }

    public static void process(BaseBusiness.HasBusiness hasBusiness, EdsAsset asset, Map<String, Object> context) {
        List<BasePostImportAssetProcessor> processors = getProcessors(
                hasBusiness.getBusinessTypeEnum(),
                EdsAssetTypeEnum.valueOf(asset.getAssetType())
        );
        if (CollectionUtils.isEmpty(processors)) {
            return;
        }
        for (BasePostImportAssetProcessor processor : processors) {
            try {
                processor.process(hasBusiness.getBusinessId(), asset, context);
            } catch (Exception e) {
                log.error(
                        "Error processing asset with processor: {}", processor.getClass()
                                .getSimpleName(), e
                );
            }
        }
    }

}
