package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientProviderFactory;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/3/27 10:34
 * @Version 1.0
 */
public interface IKubernetesClientProvider extends InitializingBean {

    String getName();

    io.fabric8.kubernetes.client.Config buildConfig(EdsKubernetesConfigModel.Kubernetes kubernetes);

    KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes);

    @Override
    default void afterPropertiesSet() {
        KubernetesClientProviderFactory.register(this);
    }

}
