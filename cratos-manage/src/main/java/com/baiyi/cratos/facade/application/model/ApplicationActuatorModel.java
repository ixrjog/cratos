package com.baiyi.cratos.facade.application.model;

import com.baiyi.cratos.common.exception.ApplicationConfigException;
import com.baiyi.cratos.common.util.YamlUtil;
import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 16:58
 * &#064;Version 1.0
 */
public class ApplicationActuatorModel {

    public static Probe to(io.fabric8.kubernetes.api.model.Probe probe) {
        if (probe == null) {
            return null;
        }
        HTTPGetAction httpGetAction = probe.getHttpGet() != null ? HTTPGetAction.builder()
                .host(probe.getHttpGet()
                        .getHost())
                .path(probe.getHttpGet()
                        .getPath())
                .port(Integer.valueOf(probe.getHttpGet()
                        .getPort()
                        .getValue()
                        .toString()))
                .scheme(probe.getHttpGet()
                        .getScheme())
                .build() : null;
        return Probe.builder()
                .failureThreshold(probe.getFailureThreshold())
                .httpGet(httpGetAction)
                .initialDelaySeconds(probe.getInitialDelaySeconds())
                .periodSeconds(probe.getPeriodSeconds())
                .successThreshold(probe.getSuccessThreshold())
                .terminationGracePeriodSeconds(probe.getTerminationGracePeriodSeconds())
                .timeoutSeconds(probe.getTimeoutSeconds())
                .build();
    }

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

    public static Lifecycle to(io.fabric8.kubernetes.api.model.Lifecycle lifecycle) {
        if (lifecycle == null) {
            return null;
        }
        LifecycleHandler postStart = to(lifecycle.getPostStart());
        LifecycleHandler preStop = to(lifecycle.getPreStop());
        return Lifecycle.builder()
                .postStart(postStart)
                .preStop(preStop)
                .build();
    }

    private static LifecycleHandler to(io.fabric8.kubernetes.api.model.LifecycleHandler lifecycleHandler) {
        if (lifecycleHandler == null) {
            return null;
        }
        return LifecycleHandler.builder()
                .exec(ExecAction.builder()
                        .command(lifecycleHandler.getExec()
                                .getCommand())
                        .build())
                .build();
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
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExecAction {
        private List<String> command;
    }

}
