package com.baiyi.cratos.wrapper.application.converter;

import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;
import com.baiyi.cratos.facade.application.baseline.mode.DeploymentBaselineModel;
import com.google.common.base.Joiner;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/2 10:13
 * &#064;Version 1.0
 */
public class BaselineConverter {

    public static ApplicationResourceBaselineVO.EnvVar toEnv(String content) {
        DeploymentBaselineModel.EnvVar envVarMO = DeploymentBaselineModel.envVarLoadAs(content);
        ApplicationResourceBaselineVO.ConfigMapKeySelector configMapKeyRef = ApplicationResourceBaselineVO.ConfigMapKeySelector.builder()
                .name(envVarMO.getValueFrom()
                        .getConfigMapKeyRef()
                        .getName())
                .key(envVarMO.getValueFrom()
                        .getConfigMapKeyRef()
                        .getKey())
                .build();
        ApplicationResourceBaselineVO.EnvVarSource valueFrom = ApplicationResourceBaselineVO.EnvVarSource.builder()
                .configMapKeyRef(configMapKeyRef)
                .build();
        return ApplicationResourceBaselineVO.EnvVar.builder()
                .name(envVarMO.getName())
                .value(envVarMO.getValue())
                .valueFrom(valueFrom)
                .content(content)
                .build();
    }

    public static ApplicationResourceBaselineVO.Lifecycle toLifecycle(String content) {
        DeploymentBaselineModel.Lifecycle lifecycleMO = DeploymentBaselineModel.lifecycleLoadAs(content);
        String preStopExecCommand = Joiner.on(" ")
                .skipNulls()
                .join(lifecycleMO.getPreStop()
                        .getExec()
                        .getCommand());
        return ApplicationResourceBaselineVO.Lifecycle.builder()
                .preStopExecCommand(preStopExecCommand)
                .content(content)
                .build();
    }

    public static ApplicationResourceBaselineVO.Probe toProbe(String content) {
        DeploymentBaselineModel.Probe probeMO = DeploymentBaselineModel.probeLoadAs(content);
        return ApplicationResourceBaselineVO.Probe.builder()
                .path(probeMO.getHttpGet()
                        .getPath())
                .port(probeMO.getHttpGet()
                        .getPort())
                .content(content)
                .build();
    }

}
