package com.baiyi.cratos.eds.kubernetes.resource.autoscaler;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/24 上午11:32
 * &#064;Version 1.0
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"currentMetrics", "currentReplicas", "desiredReplicas", "maxReplicas", "minReplicas", "periodicity", "targetMetric", "targetRef"})
public class AdvancedHorizontalPodAutoscalerStatus implements KubernetesResource {

    @Serial
    private static final long serialVersionUID = 6239896154624539409L;

    private List<CurrentMetric> currentMetrics;
    private Integer currentReplicas;
    private Integer desiredReplicas;
    private Integer maxReplicas;
    private Integer minReplicas;
    private Integer periodicity;
    private TargetMetric targetMetric;
    private String targetRef;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentMetric {
        private Resource resource;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resource {
        private Current current;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Current {
        private Integer averageUtilization;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetMetric {
        private String name;
        private String targets;
        private String type;
    }

}
