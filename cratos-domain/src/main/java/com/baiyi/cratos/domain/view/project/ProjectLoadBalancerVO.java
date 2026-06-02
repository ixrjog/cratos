package com.baiyi.cratos.domain.view.project;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.google.gson.JsonSyntaxException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/25 18:27
 * &#064;Version 1.0
 */
public class ProjectLoadBalancerVO {

    public static LbConfig loadAs(ProjectLoadBalancer projectLoadBalancer) {
        if (projectLoadBalancer== null) {
            return LbConfig.EMPTY;
        }
        return loadAs(projectLoadBalancer.getConfig());
    }

    public static LbConfig loadAs(String config) throws JsonSyntaxException {
        if (!StringUtils.hasText(config)) {
            return LbConfig.EMPTY;
        }
        return YamlUtils.loadAs(config, LbConfig.class);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class LoadBalancer implements Serializable {

        public static final LoadBalancer NO_DATA = LoadBalancer.builder()
                .build();

        @Serial
        private static final long serialVersionUID = -8262715550046907465L;
        private String instanceName;

        private String loadBalancerType;
        private String loadBalancerName;
        private String loadBalancerId;
        private String dnsName;
        private String regionId;
        private List<Listener> listeners;
        private LbConfig lbConfig;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Listener implements Serializable {
        @Serial
        private static final long serialVersionUID = 3344408357120604031L;
        private String listenerProtocol;
        private Integer listenerPort;
        private String startPort;
        private String endPort;
        @Schema(description = "自定义监听名称")
        private String listenerDescription;
        private String serverGroupId;
        private String listenerStatus;
        private List<Server> serverGroupServers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Server implements Serializable {
        @Serial
        private static final long serialVersionUID = 6960407841082342633L;
        private String serverId;
        private String serverType;
        private String serverIp;
        private Integer port;
        private Integer weight;
        private String serverGroupId;
        private String zoneId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class LbConfig implements Serializable {
        public static final LbConfig EMPTY = LbConfig.builder()
                .build();
        @Serial
        private static final long serialVersionUID = 474150162548010019L;
        private List<TrafficRoute> routes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TrafficRoute implements Serializable {
        @Serial
        private static final long serialVersionUID = 2384436631269646480L;
        private String dnsRecord;
        private String recodeType;
        private String value;
        private String cdn;
        private Integer listenerPort;
    }

}
