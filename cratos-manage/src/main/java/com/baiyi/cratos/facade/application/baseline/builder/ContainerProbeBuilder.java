package com.baiyi.cratos.facade.application.baseline.builder;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import io.fabric8.kubernetes.api.model.ExecAction;
import io.fabric8.kubernetes.api.model.HTTPGetAction;
import io.fabric8.kubernetes.api.model.Probe;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/25 11:10
 * &#064;Version 1.0
 */
public class ContainerProbeBuilder {

    private Probe probe;

    public static ContainerProbeBuilder newBuilder() {
        return new ContainerProbeBuilder();
    }

    public ContainerProbeBuilder withProbe(Probe probe) {
        this.probe = probe;
        return this;
    }

    private KubernetesDeploymentVO.HTTPGetAction makeHttpGetAction() {
        Optional<HTTPGetAction> optionalHTTPGetAction = Optional.ofNullable(probe)
                .map(Probe::getHttpGet);
        if (optionalHTTPGetAction.isEmpty()) {
            return KubernetesDeploymentVO.HTTPGetAction.EMPTY;
        }
        return KubernetesDeploymentVO.HTTPGetAction.builder()
                .host(optionalHTTPGetAction.get()
                        .getHost())
                .port(optionalHTTPGetAction.get()
                        .getPort()
                        .getValue()
                        .toString())
                .path(optionalHTTPGetAction.get()
                        .getPath())
                .scheme(optionalHTTPGetAction.get()
                        .getScheme())
                .httpHeaders(optionalHTTPGetAction.get()
                        .getHttpHeaders()
                        .stream()
                        .map(e -> KubernetesDeploymentVO.HTTPHeader.builder()
                                .name(e.getName())
                                .value(e.getValue())
                                .build())
                        .toList())
                .build();
    }

    private KubernetesDeploymentVO.ExecAction makeExecAction(io.fabric8.kubernetes.api.model.ExecAction execAction) {
        List<String> commands = Optional.ofNullable(execAction)
                .map(ExecAction::getCommand)
                .orElse(List.of());
        return KubernetesDeploymentVO.ExecAction.builder()
                .command(CollectionUtils.isEmpty(commands) ? null : String.join(" ", commands))
                .build();
    }

    public KubernetesDeploymentVO.Probe build() {
        if (Optional.ofNullable(probe)
                .isEmpty()) {
            return KubernetesDeploymentVO.Probe.EMPTY;
        }
        return KubernetesDeploymentVO.Probe.builder()
                .exec(makeExecAction(this.probe.getExec()))
                .httpGet(makeHttpGetAction())
                .failureThreshold(this.probe.getFailureThreshold())
                .periodSeconds(this.probe.getPeriodSeconds())
                .successThreshold(this.probe.getSuccessThreshold())
                .timeoutSeconds(this.probe.getTimeoutSeconds())
                .terminationGracePeriodSeconds(this.probe.getTerminationGracePeriodSeconds())
                .initialDelaySeconds(this.probe.getInitialDelaySeconds())
                .terminationGracePeriodSeconds(this.probe.getTerminationGracePeriodSeconds())
                .build();
    }

}
