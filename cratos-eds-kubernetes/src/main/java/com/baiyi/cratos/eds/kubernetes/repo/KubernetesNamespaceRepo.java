package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/24 9:11 下午
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesNamespaceRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public List<Namespace> list(EdsConfigs.Kubernetes kubernetes) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            NamespaceList namespaceList = kc.namespaces()
                    .list();
            return namespaceList.getItems()
                    .stream()
                    .filter(e -> filter(kubernetes, e))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Namespace get(EdsConfigs.Kubernetes kubernetes, String namespace) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.namespaces()
                    .withName(namespace)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private boolean filter(EdsConfigs.Kubernetes kubernetes, Namespace namespace) {
        List<String> namespaceExclude = Optional.of(kubernetes)
                .map(EdsConfigs.Kubernetes::getFilter)
                .map(EdsKubernetesConfigModel.Filter::getNamespace)
                .map(EdsKubernetesConfigModel.Namespace::getExclude)
                .orElse(Collections.emptyList());
        if (CollectionUtils.isEmpty(namespaceExclude)) {
            return true;
        }
        return namespaceExclude.stream()
                .noneMatch(s -> namespace.getMetadata()
                        .getName()
                        .equalsIgnoreCase(s));
    }

}