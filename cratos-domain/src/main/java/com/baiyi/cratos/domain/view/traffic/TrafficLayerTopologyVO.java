package com.baiyi.cratos.domain.view.traffic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 14:11
 * &#064;Version 1.0
 */
public class TrafficLayerTopologyVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Topology implements Serializable {
        public static final Topology NO_DATA = Topology.builder()
                .build();
        @Serial
        private static final long serialVersionUID = 316351778361759440L;

        public static Topology of(String serviceName, Map<String, TrafficLayerTopologyVO.TrafficPath> trafficPathMap) {
            return TrafficLayerTopologyVO.Topology.builder()
                    .serviceName(serviceName)
                    .trafficPaths(trafficPathMap.entrySet()
                                          .stream()
                                          .sorted(Map.Entry.comparingByKey())
                                          .map(Map.Entry::getValue)
                                          .toList())
                    .build();
        }

        private String serviceName;
        @Builder.Default
        private List<TrafficPath> trafficPaths = List.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TrafficPath implements Serializable {
        @Serial
        private static final long serialVersionUID = 699033722141498791L;
        // Key 域名
        private Map<String, Route> routeMap;
        private LoadBalancer loadBalancer;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Route implements Serializable {
        @Serial
        private static final long serialVersionUID = -1778625085748173963L;
        // 记录
        private String record;
        // CDN Proxy
        private String cdn;
        // 代理
        private Boolean proxied;
        // 主机标头覆盖，回源Host
        private String host;
        private String namespace;
        // 源站
        private String originServer;
        private List<String> rules;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LoadBalancer implements Serializable {
        @Serial
        private static final long serialVersionUID = 5882323898979841878L;
        private String dnsName;
        private String loadBalancerName;
        private Integer assetId;
        // private ProjectLoadBalancerVO.LoadBalancer loadBalancerDetails;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Rule implements Serializable {
        @Serial
        private static final long serialVersionUID = 7582413272656204094L;
        private String record;
        private String path;

        public List<String> getPaths() {
            List<String> paths = new ArrayList<>();
            paths.add(path);
            return paths;
        }

        public static Rule parse(String rule) {
            if (rule == null) {
                return null;
            }
            int idx = rule.indexOf("->");
            if (idx < 0) {
                return null; // 不含分隔符,非法规则
            }
            String record = rule.substring(0, idx)
                    .trim();
            String path = rule.substring(idx + 2)
                    .trim();
            if (record.isEmpty()) {
                return null;
            }
            if (path.isEmpty()) {
                path = "/";
            }
            return Rule.builder()
                    .record(record)
                    .path(path)
                    .build();
        }

    }

}
