package com.baiyi.cratos.eds.kubernetes.client.istio;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import io.fabric8.istio.client.IstioClient;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2023/10/7 15:24
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class IstioClientBuilder {

    public static IstioClient build(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return IstioClientFactory.newClient(kubernetes);
    }

}