package com.baiyi.cratos.eds.kubernetes.repo.version;

import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.VersionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 15:15
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesVersionRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public VersionInfo getVersion(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.getKubernetesVersion();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}