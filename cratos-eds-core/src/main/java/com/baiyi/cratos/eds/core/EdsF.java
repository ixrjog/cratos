package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 16:59
 * &#064;Version 1.0
 */
public class EdsF {

    private static final Map<String, Map<String, EdsInstanceAssetProvider<? super IEdsConfigModel, ?>>> CONTEXT = new ConcurrentHashMap<>();


//    public static <C extends IEdsConfigModel, A> void register(
//            EdsInstanceAssetProvider<C, A> providerBean) {
//
//        if (CONTEXT.containsKey(providerBean.getInstanceType())) {
//            Map<String, EdsInstanceAssetProvider<? super IEdsConfigModel, ?>> providerMap = CONTEXT.get(
//                    providerBean.getInstanceType());
//
//
//            providerMap.put(providerBean.getAssetType(), providerBean);
//        } else {
//            Map<String, EdsInstanceAssetProvider<? extends IEdsConfigModel, ?>> providerMap = Maps.newHashMap();
//            providerMap.put(providerBean.getAssetType(), providerBean);
//            CONTEXT.put(providerBean.getInstanceType(), providerMap);
//        }
//    }



}
