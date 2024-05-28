package com.baiyi.cratos.shell.context;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.ssh.core.model.PodAssetModel;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/28 上午11:18
 * &#064;Version 1.0
 */
public class PodAssetContext {

    private static final ThreadLocal<Map<Integer, PodAssetModel>> POD_CONTEXT = new ThreadLocal<>();

    private static final ThreadLocal<EdsKubernetesConfigModel.Kubernetes> EDS_INSTANCE_CONFIG_CONTEXT = new ThreadLocal<>();

    public static void setContext(Map<Integer, PodAssetModel> podContext, EdsKubernetesConfigModel.Kubernetes configContext) {
        POD_CONTEXT.set(podContext);
        EDS_INSTANCE_CONFIG_CONTEXT.set(configContext);
    }

    public static Map<Integer, PodAssetModel> getPodContext() {
        return POD_CONTEXT.get();
    }

    public static EdsKubernetesConfigModel.Kubernetes getConfigContext() {
        return EDS_INSTANCE_CONFIG_CONTEXT.get();
    }

    public static void remove() {
        POD_CONTEXT.remove();
        EDS_INSTANCE_CONFIG_CONTEXT.remove();
    }

}
