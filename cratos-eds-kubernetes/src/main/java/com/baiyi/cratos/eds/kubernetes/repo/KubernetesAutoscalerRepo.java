package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.resource.AdvancedHorizontalPodAutoscaler;
import com.baiyi.cratos.eds.kubernetes.resource.autoscaler.AdvancedHorizontalPodAutoscalerList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/22 下午1:40
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesAutoscalerRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public AdvancedHorizontalPodAutoscaler create(EdsKubernetesConfigModel.Kubernetes kubernetes,
                                                  AdvancedHorizontalPodAutoscaler autoscaler) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            MixedOperation<AdvancedHorizontalPodAutoscaler, AdvancedHorizontalPodAutoscalerList, Resource<AdvancedHorizontalPodAutoscaler>> autoscalerClient = kc.resources(
                    AdvancedHorizontalPodAutoscaler.class, AdvancedHorizontalPodAutoscalerList.class);
            return autoscalerClient.inNamespace(autoscaler.getMetadata()
                            .getNamespace())
                    .resource(autoscaler)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public List<Resource<AdvancedHorizontalPodAutoscaler>> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            MixedOperation<AdvancedHorizontalPodAutoscaler, AdvancedHorizontalPodAutoscalerList, Resource<AdvancedHorizontalPodAutoscaler>> autoscalerClient = kc.resources(
                    AdvancedHorizontalPodAutoscaler.class, AdvancedHorizontalPodAutoscalerList.class);
            return autoscalerClient.inNamespace(namespace)
                    .resources().toList();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
