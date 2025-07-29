package com.baiyi.cratos.eds.kubernetes.repo.resource;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/29 13:40
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesResourceCtrl {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public List<HasMetadata> createResources(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace,
                                             String resourceContent) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
            return kc.load(is)
                    .inNamespace(namespace)
                    .create();
        }
    }

    public List<HasMetadata> createResources(EdsKubernetesConfigModel.Kubernetes kubernetes, String resourceContent) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
            return kc.load(is)
                    .create();
        }
    }

    public List<StatusDetails> deleteResources(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace,
                                               String resourceContent) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
            return kc.load(is)
                    .inNamespace(namespace)
                    .delete();
        }
    }

    public List<StatusDetails> deleteResources(EdsKubernetesConfigModel.Kubernetes kubernetes, String resourceContent) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
            return kc.load(is)
                    .delete();
        }
    }

}
