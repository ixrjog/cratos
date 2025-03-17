package com.baiyi.cratos.facade.application.baseline.mode;

import com.baiyi.cratos.common.exception.ApplicationConfigException;
import com.baiyi.cratos.domain.YamlUtil;
import com.baiyi.cratos.domain.YamlDump;
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

        public static boolean validate(Probe probe1, Probe probe2) {
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
        private LifecycleHandler postStart;
        private LifecycleHandler preStop;

        public static boolean validate(Lifecycle lifecycle1, Lifecycle lifecycle2) {
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
    }

    public static Lifecycle lifecycleLoadAs(String lifecycle) {
        if (StringUtils.isBlank(lifecycle)) {
            return Lifecycle.EMPTY;
        }
        try {
            return YamlUtil.loadAs(lifecycle, Lifecycle.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application resource container lifecycle format error: {}", e.getMessage());
        }
    }

    public static Probe probeLoadAs(String probe) {
        if (StringUtils.isBlank(probe)) {
            return Probe.EMPTY;
        }
        try {
            return YamlUtil.loadAs(probe, Probe.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application resource container probe format error: {}", e.getMessage());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LifecycleHandler {
        private ExecAction exec;

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

    public static EnvVar envVarLoadAs(String envVar) {
        if (StringUtils.isBlank(envVar)) {
            return EnvVar.EMPTY;
        }
        try {
            return YamlUtil.loadAs(envVar, EnvVar.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application resource container envVar format error: {}", e.getMessage());
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnvVar extends YamlDump {
        public static final EnvVar EMPTY = EnvVar.builder()
                .build();
        private String name;
        private String value;
        private EnvVarSource valueFrom;

        public static boolean validate(EnvVar env1, EnvVar env2) {
            Optional<ConfigMapKeySelector> configMapKeySelector1 = Optional.ofNullable(env1)
                    .map(EnvVar::getValueFrom)
                    .map(EnvVarSource::getConfigMapKeyRef);

            Optional<ConfigMapKeySelector> configMapKeySelector2 = Optional.ofNullable(env2)
                    .map(EnvVar::getValueFrom)
                    .map(EnvVarSource::getConfigMapKeyRef);

            if (configMapKeySelector1.isPresent() && configMapKeySelector2.isPresent()) {
                return configMapKeySelector1.get()
                        .getKey()
                        .equals(configMapKeySelector2.get()
                                .getKey()) && configMapKeySelector1.get()
                        .getName()
                        .equals(configMapKeySelector2.get()
                                .getName());
            }
            return false;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnvVarSource {
        private ConfigMapKeySelector configMapKeyRef;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigMapKeySelector {
        private String key;
        private String name;
        private Boolean optional;
    }

}
