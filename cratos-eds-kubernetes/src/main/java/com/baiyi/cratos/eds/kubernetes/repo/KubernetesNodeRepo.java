package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public  List<Node> list(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            NodeList nodeList = kc.nodes().list();
            return nodeList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
