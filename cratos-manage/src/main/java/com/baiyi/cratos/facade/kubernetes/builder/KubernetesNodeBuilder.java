package com.baiyi.cratos.facade.kubernetes.builder;

import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeStatus;
import io.fabric8.kubernetes.api.model.Quantity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 14:24
 * &#064;Version 1.0
 */
public class KubernetesNodeBuilder {

    private static final String TOPOLOGY_KUBERNETES_IO_ZONE = "topology.kubernetes.io/zone";
    private static final String TOPOLOGY_KUBERNETES_IO_REGION = "topology.kubernetes.io/region";
    private static final String FAILURE_DOMAIN_BETA_KUBERNETES_IO_ZONE = "failure-domain.beta.kubernetes.io/zone";
    private static final String FAILURE_DOMAIN_BETA_KUBERNETES_IO_REGION = "failure-domain.beta.kubernetes.io/region";
    private Node node;

    public static KubernetesNodeBuilder newBuilder() {
        return new KubernetesNodeBuilder();
    }

    public KubernetesNodeBuilder withNode(Node node) {
        this.node = node;
        return this;
    }

    private Map<String, KubernetesNodeVO.NodeAddress> makeStatusAddresses() {
        return this.node.getStatus()
                .getAddresses()
                .stream()
                .map(e -> KubernetesNodeVO.NodeAddress.builder()
                        .address(e.getAddress())
                        .type(e.getType())
                        .build())
                .collect(Collectors.toMap(KubernetesNodeVO.NodeAddress::getType, a -> a, (k1, k2) -> k1));
    }

    private Map<String, KubernetesNodeVO.NodeCondition> makeStatusConditions() {
        return this.node.getStatus()
                .getConditions()
                .stream()
                .map(e -> KubernetesNodeVO.NodeCondition.builder()
                        .lastTransitionTime(ConverterUtil.parse(e.getLastTransitionTime()))
                        .lastHeartbeatTime(ConverterUtil.parse(e.getLastTransitionTime()))
                        .reason(e.getReason())
                        .message(e.getMessage())
                        .status(e.getStatus())
                        .type(e.getType())
                        .build())
                .collect(Collectors.toMap(KubernetesNodeVO.NodeCondition::getType, a -> a, (k1, k2) -> k1));
    }

    private KubernetesNodeVO.NodeStatus makeStatus() {
        return KubernetesNodeVO.NodeStatus.builder()
                .addresses(makeStatusAddresses())
                .nodeInfo(BeanCopierUtil.copyProperties(this.node.getStatus()
                        .getNodeInfo(), KubernetesNodeVO.NodeSystemInfo.class))
                .conditions(makeStatusConditions())
                .build();
    }

    private Map<String, String> getMetadataLabels() {
        return this.node.getMetadata()
                .getLabels();
    }

    private String getZone() {
        return getMetadataLabels().containsKey(TOPOLOGY_KUBERNETES_IO_ZONE) ? getMetadataLabels().get(
                TOPOLOGY_KUBERNETES_IO_ZONE) : getMetadataLabels().get(FAILURE_DOMAIN_BETA_KUBERNETES_IO_ZONE);
    }

    private String getRegion() {
        return getMetadataLabels().containsKey(TOPOLOGY_KUBERNETES_IO_REGION) ? getMetadataLabels().get(
                TOPOLOGY_KUBERNETES_IO_REGION) : getMetadataLabels().get(FAILURE_DOMAIN_BETA_KUBERNETES_IO_REGION);
    }

    private Map<String, String> makeAttributes() {
        Map<String, String> attributes = Maps.newHashMap();
        Optional<Map<String, Quantity>> optionalMap = Optional.of(this.node)
                .map(Node::getStatus)
                .map(NodeStatus::getCapacity);
        if (optionalMap.isEmpty()) {
            return attributes;
        }
        Map<String, Quantity> quantityMap = optionalMap.get();
        if (quantityMap.containsKey("cpu")) {
            attributes.put(KUBERNETES_NODE_CPU, formatQuantity(quantityMap.get("cpu")));
        }
        if (quantityMap.containsKey("ephemeral-storage")) {
            attributes.put(KUBERNETES_NODE_CAPACITY_EPHEMERAL_STORAGE,
                    formatQuantity(quantityMap.get("ephemeral-storage")));
        }
        if (quantityMap.containsKey("memory")) {
            attributes.put(KUBERNETES_NODE_CAPACITY_MEMORY, formatQuantity(quantityMap.get("memory")));
        }
        return attributes;
    }

    private String formatQuantity(Quantity quantity) {
        return Joiner.on("/")
                .skipNulls()
                .join(quantity.getAmount(), quantity.getFormat());
    }

    public KubernetesNodeVO.Node build() {
        return KubernetesNodeVO.Node.builder()
                .zone(getZone())
                .region(getRegion())
                .metadata(ConverterUtil.toMetadata(this.node.getMetadata()))
                .status(makeStatus())
                .attributes(makeAttributes())
                .build();
    }

}
