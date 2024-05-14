package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.baiyi.cratos.eds.core.exception.EdsInstanceProviderException;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Eds instance provider factory
 *
 * @Author baiyi
 * @Date 2024/2/23 17:30
 * @Version 1.0
 */
@Slf4j
public class EdsInstanceProviderFactory {

    private EdsInstanceProviderFactory() {
    }

    private static final Map<String, Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>>> CONTEXT = new ConcurrentHashMap<>();

    public static <C extends IEdsConfigModel, A> void register(EdsInstanceAssetProvider<C, A> providerBean) {
        log.info("EdsInstanceProviderFactory Registered: instanceType={}, assetType={}", providerBean.getInstanceType(), providerBean.getAssetType());
        if (CONTEXT.containsKey(providerBean.getInstanceType())) {
            Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> providerMap = CONTEXT.get(providerBean.getInstanceType());
            providerMap.put(providerBean.getAssetType(), providerBean);
        } else {
            Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> providerMap = Maps.newHashMap();
            providerMap.put(providerBean.getAssetType(), providerBean);
            CONTEXT.put(providerBean.getInstanceType(), providerMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static <A> A produceModel(String instanceType, String assetType, EdsAssetVO.Asset asset) {
        return (A) EdsInstanceProviderFactory.CONTEXT.get(instanceType)
                .get(assetType)
                .assetLoadAs(asset.getOriginalModel());
    }

    @SuppressWarnings("unchecked")
    public static <C extends IEdsConfigModel> C produceConfig(String instanceType, String assetType, EdsConfig edsConfig) {
        return (C) EdsInstanceProviderFactory.CONTEXT.get(instanceType)
                .get(assetType)
                .produceConfig(edsConfig);
    }

    /**
     * 查询实例下所有资产类型
     *
     * @param instanceType
     * @return
     */
    public static Set<String> getInstanceAssetTypes(String instanceType) {
        Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> pMap = EdsInstanceProviderFactory.CONTEXT.get(instanceType);
        return pMap != null ? pMap.keySet() : Sets.newHashSet();
    }

    @SuppressWarnings("unchecked")
    public static <C extends IEdsConfigModel> C produceConfig(String instanceType, EdsConfig edsConfig) {
        try {
            Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> pMap = EdsInstanceProviderFactory.CONTEXT.get(instanceType);
            for (String assetType : pMap.keySet()) {
                return (C) pMap.get(assetType)
                        .produceConfig(edsConfig);
            }
        } catch (Exception e) {
            throw new EdsConfigException("The eds instance type is incorrect: {}.", e.getMessage());
        }
        throw new EdsConfigException("The eds instance type is incorrect.");
    }

    public static void setConfig(String instanceType, EdsConfig edsConfig) {
        if (!EdsInstanceProviderFactory.CONTEXT.containsKey(instanceType)) {
            return;
        }
        Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> providerMap = EdsInstanceProviderFactory.CONTEXT.get(instanceType);
        providerMap.keySet()
                .stream()
                .findFirst()
                .ifPresent(s -> providerMap.get(s)
                        .setConfig(edsConfig));
    }

    /**
     * build eds instance provider holder
     *
     * @param instance
     * @param assetType
     * @param <C>
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <C extends IEdsConfigModel, A> EdsInstanceProviderHolder<C, A> buildHolder(ExternalDataSourceInstance<C> instance, String assetType) {
        String instanceType = instance.getEdsInstance()
                .getEdsType();
        Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> providerMap = Optional.of(CONTEXT.get(instanceType))
                .orElseThrow(() -> new EdsInstanceProviderException("No available provider: instanceType={}.", instanceType));
        EdsInstanceAssetProvider<C, A> provider = (EdsInstanceAssetProvider<C, A>) Optional.of(providerMap.get(assetType))
                .orElseThrow(() -> new EdsInstanceProviderException("No available provider: instanceType={}, assetType={}.", instanceType, assetType));
        return EdsInstanceProviderHolder.<C, A>builder()
                .instance(instance)
                .provider(provider)
                .build();
    }

}
