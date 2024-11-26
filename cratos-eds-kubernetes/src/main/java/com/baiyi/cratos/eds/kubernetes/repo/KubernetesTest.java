package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.VersionInfo;
import io.fabric8.kubernetes.client.dsl.MetricAPIGroupDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 11:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesTest {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public void test(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            VersionInfo versionInfo = kc.getKubernetesVersion();
            MetricAPIGroupDSL dsl = kc.top();
            System.out.println(versionInfo);

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}