package com.baiyi.cratos.eds.core.version;

import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.exception.EdsInstanceVersionProviderException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceVersionProviderHolder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 14:19
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EdsInstanceVersionProviderFactory {

    private static final Map<String, BaseEdsInstanceVersionProvider<?>> CONTEXT = new ConcurrentHashMap<>();

    public static <Config extends HasEdsConfig> void register(BaseEdsInstanceVersionProvider<Config> providerBean) {
        log.info("EdsInstanceVersionProviderFactory Registered: instanceType={}", providerBean.getInstanceType());
        CONTEXT.put(providerBean.getInstanceType(), providerBean);
    }

    public static boolean isSupport(String instanceType){
        return CONTEXT.containsKey(instanceType);
    }

    @SuppressWarnings("unchecked")
    public static <Config extends HasEdsConfig> BaseEdsInstanceVersionProvider<Config> getVersionProvider(
            String instanceType) {
        return (BaseEdsInstanceVersionProvider<Config>) CONTEXT.get(instanceType);
    }

    @SuppressWarnings("unchecked")
    public static <Config extends HasEdsConfig> EdsInstanceVersionProviderHolder<Config> buildHolder(
            ExternalDataSourceInstance<Config> instance) throws EdsInstanceVersionProviderException {
        String instanceType = instance.getEdsInstance()
                .getEdsType();
        if (!CONTEXT.containsKey(instanceType)) {
            EdsInstanceVersionProviderException.runtime("No available provider: instanceType={}.", instanceType);
        }
        return EdsInstanceVersionProviderHolder.<Config>builder()
                .instance(instance)
                .provider((BaseEdsInstanceVersionProvider<Config>) CONTEXT.get(instanceType))
                .build();
    }

}
