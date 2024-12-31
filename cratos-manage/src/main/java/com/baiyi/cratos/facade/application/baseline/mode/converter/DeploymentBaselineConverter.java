package com.baiyi.cratos.facade.application.baseline.mode.converter;

import com.baiyi.cratos.common.exception.ApplicationConfigException;
import com.baiyi.cratos.common.util.YamlUtil;
import com.baiyi.cratos.facade.application.baseline.mode.DeploymentBaselineModel;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 13:39
 * &#064;Version 1.0
 */
public class DeploymentBaselineConverter {

    public static DeploymentBaselineModel.Probe to(io.fabric8.kubernetes.api.model.Probe probe) {
        if (probe == null) {
            return null;
        }
        DeploymentBaselineModel.HTTPGetAction httpGetAction = probe.getHttpGet() != null ? DeploymentBaselineModel.HTTPGetAction.builder()
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
        return DeploymentBaselineModel.Probe.builder()
                .failureThreshold(probe.getFailureThreshold())
                .httpGet(httpGetAction)
                .initialDelaySeconds(probe.getInitialDelaySeconds())
                .periodSeconds(probe.getPeriodSeconds())
                .successThreshold(probe.getSuccessThreshold())
                .terminationGracePeriodSeconds(probe.getTerminationGracePeriodSeconds())
                .timeoutSeconds(probe.getTimeoutSeconds())
                .build();
    }

    public static DeploymentBaselineModel.Lifecycle to(io.fabric8.kubernetes.api.model.Lifecycle lifecycle) {
        if (lifecycle == null) {
            return null;
        }
        DeploymentBaselineModel.LifecycleHandler postStart = to(lifecycle.getPostStart());
        DeploymentBaselineModel.LifecycleHandler preStop = to(lifecycle.getPreStop());
        return DeploymentBaselineModel.Lifecycle.builder()
                .postStart(postStart)
                .preStop(preStop)
                .build();
    }

    private static DeploymentBaselineModel.LifecycleHandler to(io.fabric8.kubernetes.api.model.LifecycleHandler lifecycleHandler) {
        if (lifecycleHandler == null) {
            return null;
        }
        return DeploymentBaselineModel.LifecycleHandler.builder()
                .exec(DeploymentBaselineModel.ExecAction.builder()
                        .command(lifecycleHandler.getExec()
                                .getCommand())
                        .build())
                .build();
    }

//    public static DeploymentBaselineModel.Lifecycle lifecycleLoadAs(ApplicationActuator actuator) {
//        return lifecycleLoadAs(actuator.getLifecycle());
//    }

    public static DeploymentBaselineModel.Lifecycle lifecycleLoadAs(String lifecycle) {
        if (StringUtils.isBlank(lifecycle)) {
            return DeploymentBaselineModel.Lifecycle.EMPTY;
        }
        try {
            return YamlUtil.loadAs(lifecycle, DeploymentBaselineModel.Lifecycle.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application actuator lifecycle format error: {}", e.getMessage());
        }
    }

    public static DeploymentBaselineModel.Probe probeLoadAs(String probe) {
        if (StringUtils.isBlank(probe)) {
            return DeploymentBaselineModel.Probe.EMPTY;
        }
        try {
            return YamlUtil.loadAs(probe, DeploymentBaselineModel.Probe.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application actuator lifecycle format error: {}", e.getMessage());
        }
    }

}
