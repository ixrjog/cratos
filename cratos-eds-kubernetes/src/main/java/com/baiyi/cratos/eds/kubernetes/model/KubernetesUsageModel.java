package com.baiyi.cratos.eds.kubernetes.model;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.Quantity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/27 13:56
 * &#064;Version 1.0
 */
public class KubernetesUsageModel {

    @Data
    @AllArgsConstructor
    public static class NodeUsage {
        private final Node node;
        private final String name;
        private final BigDecimal cpuPercentage;
        private final BigDecimal memoryPercentage;

        public NodeUsage(Node node, Map<String, Quantity> used) {
            this.node = node;
            this.name = node.getMetadata()
                    .getName();
            cpuPercentage = calculateUsage(used.get("cpu"), node.getStatus()
                    .getAllocatable()
                    .get("cpu"));
            memoryPercentage = calculateUsage(used.get("memory"), node.getStatus()
                    .getAllocatable()
                    .get("memory"));
        }

        private static BigDecimal calculateUsage(Quantity used, Quantity total) {
            return Quantity.getAmountInBytes(used)
                    .divide(Quantity.getAmountInBytes(total), 2, RoundingMode.FLOOR)
                    .multiply(BigDecimal.valueOf(100));
        }

        public KubernetesNodeVO.NodeUsage toNodeUsage() {
            return KubernetesNodeVO.NodeUsage.builder()
                    .name(this.name)
                    .cpuPercentage(this.cpuPercentage)
                    .memoryPercentage(this.memoryPercentage)
                    .build();
        }

    }

}
