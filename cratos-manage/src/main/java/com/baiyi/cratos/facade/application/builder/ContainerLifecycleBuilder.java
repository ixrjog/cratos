package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import io.fabric8.kubernetes.api.model.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/25 10:12
 * &#064;Version 1.0
 */
public class ContainerLifecycleBuilder {

    private Container container;

    public static ContainerLifecycleBuilder newBuilder() {
        return new ContainerLifecycleBuilder();
    }

    public ContainerLifecycleBuilder withContainer(Container container) {
        this.container = container;
        return this;
    }

    private KubernetesDeploymentVO.SleepAction makeSleepAction(
            io.fabric8.kubernetes.api.model.LifecycleHandler lifecycleHandler) {
        Long sleepSeconds = Optional.ofNullable(lifecycleHandler)
                .map(LifecycleHandler::getSleep)
                .map(SleepAction::getSeconds)
                .orElse(null);
        return sleepSeconds != null ? KubernetesDeploymentVO.SleepAction.builder()
                .seconds(lifecycleHandler.getSleep()
                        .getSeconds())
                .build() : null;
    }

    private KubernetesDeploymentVO.ExecAction makeExecAction(
            io.fabric8.kubernetes.api.model.LifecycleHandler lifecycleHandler) {
        List<String> commands = Optional.ofNullable(lifecycleHandler)
                .map(LifecycleHandler::getExec)
                .map(ExecAction::getCommand)
                .orElse(List.of());
        return KubernetesDeploymentVO.ExecAction.builder()
                .command(CollectionUtils.isEmpty(commands) ? null : String.join(" ", commands))
                .build();
    }

    private KubernetesDeploymentVO.LifecycleHandler makeStop(
            io.fabric8.kubernetes.api.model.LifecycleHandler lifecycleHandler) {
        return KubernetesDeploymentVO.LifecycleHandler.builder()
                .exec(makeExecAction(lifecycleHandler))
                .sleep(makeSleepAction(lifecycleHandler))
                .build();
    }

    public KubernetesDeploymentVO.Lifecycle build() {
        Optional<Lifecycle> optionalLifecycle = Optional.ofNullable(this.container)
                .map(Container::getLifecycle);
        if (optionalLifecycle.isEmpty()) {
            return KubernetesDeploymentVO.Lifecycle.EMPTY;
        }
        return KubernetesDeploymentVO.Lifecycle.builder()
                .preStop(makeStop(this.container.getLifecycle()
                        .getPreStop()))
                .postStart(makeStop(this.container.getLifecycle()
                        .getPostStart()))
                .build();
    }

}
