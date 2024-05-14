package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午1:55
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesServiceRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public List<Service> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            ServiceList serviceList = kc.services()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(serviceList.getItems())) {
                return Collections.emptyList();
            }
            return serviceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Service get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.services()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }


}
