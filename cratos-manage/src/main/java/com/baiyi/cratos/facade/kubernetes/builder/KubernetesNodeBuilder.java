package com.baiyi.cratos.facade.kubernetes.builder;

import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtil;
import io.fabric8.kubernetes.api.model.Node;

import java.util.Map;
import java.util.stream.Collectors;

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

    public KubernetesNodeVO.Node build() {
        return KubernetesNodeVO.Node.builder()
                .zone(getZone())
                .region(getRegion())
                .metadata(ConverterUtil.toMetadata(this.node.getMetadata()))
                .status(makeStatus())
                .build();
    }

}
