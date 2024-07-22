package com.baiyi.cratos.eds.kubernetes.resource.autoscaler;

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
 * &#064;Date  2024/7/22 下午2:02
 * &#064;Version 1.0
 */
@JsonDeserialize(using = JsonDeserializer.None.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedHorizontalPodAutoscalerSpec implements KubernetesResource {

    @Serial
    private static final long serialVersionUID = -7419791738322145247L;

    private String scaleStrategy;
    private List<Metric> metrics;
    private ScaleTargetRef scaleTargetRef;
    private Integer maxReplicas;
    private Integer minReplicas;
    private Integer stabilizationWindowSeconds;
    private Prediction prediction;
    private List<InstanceBound> instanceBounds;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metric {
        private String type;
        private Resource resource;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resource {
        private String name;
        private Target target;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Target {
        private String type;
        private Integer averageUtilization;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScaleTargetRef {
        private String apiVersion;
        private String kind;
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Prediction {
        private Integer quantile;
        private Integer scaleUpForward;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstanceBound {
        private String startTime;
        private String endTime;
        private List<Bound> bounds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bound {
        private String cron;
        private Integer maxReplicas;
        private Integer minReplicas;
    }

}
