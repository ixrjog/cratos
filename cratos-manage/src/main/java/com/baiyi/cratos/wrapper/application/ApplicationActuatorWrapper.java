package com.baiyi.cratos.wrapper.application;

import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.baiyi.cratos.domain.view.application.ApplicationActuatorVO;
import com.baiyi.cratos.facade.application.model.ApplicationActuatorModel;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/24 10:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationActuatorWrapper extends BaseDataTableConverter<ApplicationActuatorVO.ApplicationActuator, ApplicationActuator> implements IBaseWrapper<ApplicationActuatorVO.ApplicationActuator> {

    @Override
    public void wrap(ApplicationActuatorVO.ApplicationActuator vo) {
        ApplicationActuatorVO.Lifecycle lifecycle = toLifecycleVO(vo.getLifecycle());
        ApplicationActuatorVO.Probe startupProbe = toProbeVO(vo.getStartupProbe());
        verifyStartupProbe(vo.getFramework(), startupProbe);
        ApplicationActuatorVO.Probe livenessProbe = toProbeVO(vo.getLivenessProbe());
        verifyLivenessProbe(vo.getFramework(), livenessProbe);
        ApplicationActuatorVO.Probe readinessProbe = toProbeVO(vo.getReadinessProbe());
        verifyReadinessProbe(vo.getFramework(), readinessProbe);
        ApplicationActuatorVO.Container container = ApplicationActuatorVO.Container.builder()
                .startupProbe(startupProbe)
                .livenessProbe(livenessProbe)
                .readinessProbe(readinessProbe)
                .lifecycle(lifecycle)
                .build();
        container.verify();
        vo.setContainer(container);
        vo.setStandard(container.getStandard());
    }

    private void verifyStartupProbe(String framework, ApplicationActuatorVO.Probe startupProbe) {
        boolean portOK = startupProbe.getPort() == 8081;
        boolean pathPass = false;
        if ("PPJv1".equals(framework)) {
            pathPass = "/actuator/health".equals(startupProbe.getPath());
        }
        if ("PPJv2".equals(framework)) {
            pathPass = "/actuator/health/readiness".equals(startupProbe.getPath());
        }
        startupProbe.setStandard(portOK && pathPass);
    }

    private void verifyLivenessProbe(String framework, ApplicationActuatorVO.Probe livenessProbe) {
        boolean portOK = livenessProbe.getPort() == 8081;
        boolean pathPass = false;
        if ("PPJv1".equals(framework)) {
            pathPass = "/actuator/health".equals(livenessProbe.getPath());
        }
        if ("PPJv2".equals(framework)) {
            pathPass = "/actuator/health/liveness".equals(livenessProbe.getPath());
        }
        livenessProbe.setStandard(portOK && pathPass);
    }

    private void verifyReadinessProbe(String framework, ApplicationActuatorVO.Probe readinessProbe) {
        boolean portOK = readinessProbe.getPort() == 8081;
        boolean pathPass = false;
        if ("PPJv1".equals(framework)) {
            pathPass = "/actuator/health".equals(readinessProbe.getPath());
        }
        if ("PPJv2".equals(framework)) {
            pathPass = "/actuator/health/readiness".equals(readinessProbe.getPath());
        }
        readinessProbe.setStandard(portOK && pathPass);
    }

    private ApplicationActuatorVO.Lifecycle toLifecycleVO(String lifecycle) {
        ApplicationActuatorModel.Lifecycle lifecycleMO = ApplicationActuatorModel.lifecycleLoadAs(lifecycle);
        String preStopExecCommand = Joiner.on(" ")
                .skipNulls()
                .join(lifecycleMO.getPreStop()
                        .getExec()
                        .getCommand());
        return ApplicationActuatorVO.Lifecycle.builder()
                .preStopExecCommand(preStopExecCommand)
                .standard(preStopExecCommand.contains("http://127.0.0.1:8081/actuator/eksshutdown"))
                .build();
    }

    private ApplicationActuatorVO.Probe toProbeVO(String probe) {
        ApplicationActuatorModel.Probe probeMO = ApplicationActuatorModel.probeLoadAs(probe);
        return ApplicationActuatorVO.Probe.builder()
                .path(probeMO.getHttpGet()
                        .getPath())
                .port(probeMO.getHttpGet()
                        .getPort())
                .build();
    }

}