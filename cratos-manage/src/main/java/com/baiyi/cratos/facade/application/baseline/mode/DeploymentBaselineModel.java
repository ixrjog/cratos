package com.baiyi.cratos.facade.application.baseline.mode;

import com.baiyi.cratos.common.exception.ApplicationConfigException;
import com.baiyi.cratos.common.util.YamlUtil;
import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 16:58
 * &#064;Version 1.0
 */
public class DeploymentBaselineModel {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Probe extends YamlDump {
        public static final Probe EMPTY = Probe.builder()
                .build();
        private Integer failureThreshold;
        private HTTPGetAction httpGet;
        private Integer initialDelaySeconds;
        private Integer periodSeconds;
        private Integer successThreshold;
        // private TCPSocketAction tcpSocket;
        private Long terminationGracePeriodSeconds;
        private Integer timeoutSeconds;

        public static boolean equals(Probe probe1, Probe probe2) {
            int port1 = Optional.ofNullable(probe1)
                    .map(Probe::getHttpGet)
                    .map(HTTPGetAction::getPort)
                    .orElse(0);
            int port2 = Optional.ofNullable(probe2)
                    .map(Probe::getHttpGet)
                    .map(HTTPGetAction::getPort)
                    .orElse(0);
            String host1 = Optional.ofNullable(probe1)
                    .map(Probe::getHttpGet)
                    .map(HTTPGetAction::getHost)
                    .orElse("");
            String host2 = Optional.ofNullable(probe2)
                    .map(Probe::getHttpGet)
                    .map(HTTPGetAction::getHost)
                    .orElse("");
            return port1 == port2 && host1.equals(host2);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HTTPGetAction {
        private String host;
        private String path;
        private Integer port;
        private String scheme;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Lifecycle extends YamlDump {
        public static final Lifecycle EMPTY = Lifecycle.builder()
                .build();

        public static boolean equals(Lifecycle lifecycle1, Lifecycle lifecycle2) {
            String command1 = Optional.ofNullable(lifecycle1)
                    .map(Lifecycle::getPreStop)
                    .map(LifecycleHandler::getCommand)
                    .orElse("");

            String command2 = Optional.ofNullable(lifecycle2)
                    .map(Lifecycle::getPreStop)
                    .map(LifecycleHandler::getCommand)
                    .orElse("");
            return command1.equals(command2);
        }

        private LifecycleHandler postStart;
        private LifecycleHandler preStop;
    }

    public static Lifecycle lifecycleLoadAs(ApplicationActuator actuator) {
        return lifecycleLoadAs(actuator.getLifecycle());
    }

    public static Lifecycle lifecycleLoadAs(String lifecycle) {
        if (StringUtils.isBlank(lifecycle)) {
            return Lifecycle.EMPTY;
        }
        try {
            return YamlUtil.loadAs(lifecycle, Lifecycle.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application actuator lifecycle format error: {}", e.getMessage());
        }
    }

    public static Probe probeLoadAs(String probe) {
        if (StringUtils.isBlank(probe)) {
            return Probe.EMPTY;
        }
        try {
            return YamlUtil.loadAs(probe, Probe.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application actuator lifecycle format error: {}", e.getMessage());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LifecycleHandler {
        private ExecAction exec;
        // private io.fabric8.kubernetes.api.model.HTTPGetAction httpGet;

        public String getCommand() {
            List<String> command = Optional.ofNullable(exec)
                    .map(ExecAction::getCommand)
                    .orElse(List.of());
            return StringUtils.join(command, " ");
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExecAction {
        private List<String> command;
    }

}
