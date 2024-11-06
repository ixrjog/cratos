package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/5 15:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesCustomResourceRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public CustomResourceDefinition create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            CustomResourceDefinition crd = kc.apiextensions()
                    .v1()
                    .customResourceDefinitions()
                    .load(is)
                    .create();
            return kc.apiextensions()
                    .v1()
                    .customResourceDefinitions()
                    .resource(crd)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
