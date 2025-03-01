package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.model.KubernetesUsageModel;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.VersionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public void test1(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            VersionInfo versionInfo = kc.getKubernetesVersion();
            System.out.println(versionInfo);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public void test2(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Map<String, Node> nodeMap = kc.nodes().list().getItems()
                    .stream()
                    .collect(Collectors.toMap(node -> node.getMetadata().getName(), Function.identity()));
            List<KubernetesUsageModel.NodeUsage> usageList = kc.top().nodes().metrics().getItems()
                    .stream()
                    .map(metric -> new KubernetesUsageModel.NodeUsage(nodeMap.get(metric.getMetadata().getName()), metric.getUsage()))
                    .collect(Collectors.toList());
            System.out.println(usageList);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}