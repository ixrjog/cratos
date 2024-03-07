package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.MyKubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
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
public class KubernetesNamespaceRepo {

    public static List<Namespace> list(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
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

    public static Namespace get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = MyKubernetesClientBuilder.build(kubernetes)) {
            return kc.namespaces()
                    .withName(namespace)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private static boolean filter(EdsKubernetesConfigModel.Kubernetes kubernetes, Namespace namespace) {
        List<String> namespaceExclude = Optional.of(kubernetes)
                .map(EdsKubernetesConfigModel.Kubernetes::getFilter)
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