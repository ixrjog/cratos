package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.model.KubernetesUsageModel;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午2:41
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesNodeRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public List<Node> list(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            NodeList nodeList = kc.nodes()
                    .list();
            return nodeList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Map<String, KubernetesNodeVO.NodeUsage> queryNodeUsageMap(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Map<String, Node> nodeMap = kc.nodes()
                    .list()
                    .getItems()
                    .stream()
                    .collect(Collectors.toMap(node -> node.getMetadata()
                            .getName(), Function.identity()));
            return kc.top()
                    .nodes()
                    .metrics()
                    .getItems()
                    .stream()
                    .map(metric -> new KubernetesUsageModel.NodeUsage(nodeMap.get(metric.getMetadata()
                            .getName()), metric.getUsage()).toNodeUsage())
                    .collect(Collectors.toMap(KubernetesNodeVO.NodeUsage::getName, a -> a, (k1, k2) -> k1));
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
