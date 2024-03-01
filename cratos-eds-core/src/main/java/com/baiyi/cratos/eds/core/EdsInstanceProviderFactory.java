package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.eds.core.exception.EdsInstanceProviderException;
import com.baiyi.cratos.eds.core.support.EdsInstanceProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
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

    private static final Map<String, Map<String, EdsInstanceProvider<? extends IEdsConfigModel, ?>>> CONTEXT = new ConcurrentHashMap<>();

    public static <C extends IEdsConfigModel, A> void register(EdsInstanceProvider<C, A> providerBean) {
        log.info("EdsInstanceProviderFactory Registered: instanceType={}, assetType={}", providerBean.getInstanceType(), providerBean.getAssetType());
        if (CONTEXT.containsKey(providerBean.getInstanceType())) {
            Map<String, EdsInstanceProvider<? extends IEdsConfigModel, ?>> providerMap = CONTEXT.get(providerBean.getInstanceType());
            providerMap.put(providerBean.getAssetType(), providerBean);
        } else {
            Map<String, EdsInstanceProvider<? extends IEdsConfigModel, ?>> providerMap = Maps.newHashMap();
            providerMap.put(providerBean.getAssetType(), providerBean);
            CONTEXT.put(providerBean.getInstanceType(), providerMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static <C extends IEdsConfigModel> C produce(String instanceType, String assetType, EdsConfig edsConfig) {
        return (C) EdsInstanceProviderFactory.CONTEXT.get(instanceType)
                .get(assetType)
                .produce(edsConfig);
    }

    /**
     * build eds instance provider delegate
     *
     * @param instance
     * @param assetType
     * @param <C>
     * @param <A>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <C extends IEdsConfigModel, A> EdsInstanceProviderDelegate<C, A> buildDelegate(ExternalDataSourceInstance<C> instance, String assetType) {
        String instanceType = instance.getEdsInstance()
                .getEdsType();
        Map<String, EdsInstanceProvider<? extends IEdsConfigModel, ?>> providerMap = Optional.of(CONTEXT.get(instanceType))
                .orElseThrow(() -> new EdsInstanceProviderException("No available provider: instanceType={}.", instanceType));
        EdsInstanceProvider<C, A> provider = (EdsInstanceProvider<C, A>) Optional.of(providerMap.get(assetType))
                .orElseThrow(() -> new EdsInstanceProviderException("No available provider: instanceType={}, assetType={}.", instanceType, assetType));
        return EdsInstanceProviderDelegate.<C, A>builder()
                .instance(instance)
                .provider(provider)
                .build();
    }

}
