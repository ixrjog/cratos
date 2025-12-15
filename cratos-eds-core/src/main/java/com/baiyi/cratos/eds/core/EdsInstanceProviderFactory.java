package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.baiyi.cratos.eds.core.exception.EdsInstanceProviderException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
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
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EdsInstanceProviderFactory {

    private static final Map<String, Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>>> CONTEXT = new ConcurrentHashMap<>();

    public static <Config extends HasEdsConfig, Asset> void register(
            EdsInstanceAssetProvider<Config, Asset> providerBean) {
        log.info("EdsInstanceProviderFactory Registered: instanceType={}, assetType={}", providerBean.getInstanceType(),
                providerBean.getAssetType());
        if (CONTEXT.containsKey(providerBean.getInstanceType())) {
            Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>> providerMap = CONTEXT.get(
                    providerBean.getInstanceType());
            providerMap.put(providerBean.getAssetType(), providerBean);
        } else {
            Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>> providerMap = Maps.newHashMap();
            providerMap.put(providerBean.getAssetType(), providerBean);
            CONTEXT.put(providerBean.getInstanceType(), providerMap);
        }
    }

    public static <Asset> Asset produceModel(String instanceType, String assetType, EdsAssetVO.Asset asset) {
        return (Asset) EdsInstanceProviderFactory.CONTEXT.get(instanceType)
                .get(assetType)
                .assetLoadAs(asset.getOriginalModel());
    }

    public static <Config extends HasEdsConfig> Config produceConfig(String instanceType, String assetType,
                                                                     EdsConfig edsConfig) {
        return (Config) EdsInstanceProviderFactory.CONTEXT.get(instanceType)
                .get(assetType)
                .configLoadAs(edsConfig);
    }

    /**
     * 查询实例下所有资产类型
     *
     * @param instanceType
     * @return
     */
    public static Set<String> getInstanceAssetTypes(String instanceType) {
        Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>> pMap = EdsInstanceProviderFactory.CONTEXT.get(
                instanceType);
        return pMap != null ? pMap.keySet() : Sets.newHashSet();
    }

    public static <Config extends HasEdsConfig> Config produceConfig(String instanceType, EdsConfig edsConfig) {
        try {
            Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>> pMap = EdsInstanceProviderFactory.CONTEXT.get(
                    instanceType);
            for (String assetType : pMap.keySet()) {
                return (Config) pMap.get(assetType)
                        .configLoadAs(edsConfig);
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
        Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>> providerMap = EdsInstanceProviderFactory.CONTEXT.get(
                instanceType);
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
     * @param <Config>
     * @param <Asset>
     * @return
     */
    public static <Config extends HasEdsConfig, Asset> EdsInstanceProviderHolder<Config, Asset> buildHolder(
            ExternalDataSourceInstance<Config> instance, String assetType) {
        final String instanceType = instance.getEdsInstance()
                .getEdsType();
        if (!CONTEXT.containsKey(instanceType)) {
            EdsInstanceProviderException.runtime("No available provider: instanceType={}.", instanceType);
        }
        Map<String, EdsInstanceAssetProvider<? extends HasEdsConfig, ?>> providerMap = CONTEXT.get(instanceType);
        if (!providerMap.containsKey(assetType)) {
            EdsInstanceProviderException.runtime("No available provider: instanceType={}, assetType={}.", instanceType,
                    assetType);
        }
        EdsInstanceAssetProvider<Config , Asset> provider = (EdsInstanceAssetProvider<Config, Asset>) providerMap.get(
                assetType);
        return EdsInstanceProviderHolder.<Config, Asset>builder()
                .instance(instance)
                .provider(provider)
                .build();
    }

}
