package com.baiyi.cratos.eds.kubernetes.client;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.provider.AmazonEksProvider;
import com.baiyi.cratos.eds.kubernetes.client.provider.DefaultKubernetesProvider;
import com.baiyi.cratos.eds.kubernetes.client.provider.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesException;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2021/6/24 5:07 下午
 * @Version 1.0
 */
@Slf4j
public class MyKubernetesClientBuilder {

    public interface Values {
        int CONNECTION_TIMEOUT = 60 * 1000;
        int REQUEST_TIMEOUT = 60 * 1000;
        int WEBSOCKET_TIMEOUT = 60 * 1000;
    }

    public static KubernetesClient build(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        if (StringUtils.isNotBlank(kubernetes.getProvider())) {
            return buildWithProvider(kubernetes);
        }
        return DefaultKubernetesProvider.buildClient(kubernetes);
    }

    /**
     * 按供应商构建 client
     *
     * @param kubernetes
     * @return
     */
    private static KubernetesClient buildWithProvider(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        if (KubernetesProvidersEnum.AMAZON_EKS.getDesc().equalsIgnoreCase(kubernetes.getProvider())) {
            try {
                return AmazonEksProvider.buildClientWithProvider(kubernetes);
            } catch (URISyntaxException e) {
                throw new KubernetesException("KubernetesClient error: {}", e.getMessage());
            }
        }
        throw new KubernetesException("无效的供应商 {}", kubernetes.getProvider());
    }

}