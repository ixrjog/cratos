package com.baiyi.cratos.facade.application.baseline.processor;

import com.baiyi.cratos.common.enums.PPFramework;
import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.facade.application.baseline.mode.DeploymentBaselineModel;
import com.baiyi.cratos.facade.application.baseline.mode.converter.DeploymentBaselineConverter;
import com.baiyi.cratos.facade.application.baseline.processor.base.BaseContainerBaselineMemberProcessor;
import com.baiyi.cratos.service.ApplicationResourceBaselineMemberService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 15:09
 * &#064;Version 1.0
 */
@Component
public class ContainerLifecycleBaselineMemberProcessor extends BaseContainerBaselineMemberProcessor {

    public ContainerLifecycleBaselineMemberProcessor(ApplicationResourceBaselineMemberService baselineMemberService) {
        super(baselineMemberService);
    }

    @Override
    public ResourceBaselineTypeEnum getType() {
        return ResourceBaselineTypeEnum.CONTAINER_LIFECYCLE;
    }

    @Override
    public void saveMember(ApplicationResourceBaseline baseline, Container container) {
        DeploymentBaselineModel.Lifecycle lifecycle = DeploymentBaselineConverter.to(Optional.of(container)
                .map(Container::getLifecycle)
                .orElse(null));
        DeploymentBaselineModel.Lifecycle baselineLifecycle = generateBaselineContent(baseline.getFramework());
        ApplicationResourceBaselineMember lifecycleMember = ApplicationResourceBaselineMember.builder()
                .baselineId(baseline.getId())
                .baselineType(getType().name())
                .name(getType().getDisplayName())
                .applicationName(baseline.getApplicationName())
                .namespace(baseline.getNamespace())
                .content(lifecycle.dump())
                .baselineContent(baselineLifecycle.dump())
                .standard(DeploymentBaselineModel.Lifecycle.validate(lifecycle, baselineLifecycle))
                .build();
        save(lifecycleMember);
    }

    @Override
    public void mergeToBaseline(ApplicationResourceBaseline baseline, ApplicationResourceBaselineMember baselineMember,
                                Deployment deployment, Container container) {
        if (PPFramework.PP_JV_SPRINGBOOT_2.getDisplayName()
                .equals(baseline.getFramework())) {
            List<String> command = List.of("curl", "http://127.0.0.1:8081/actuator/shutdown", "-X", "POST");
            container.getLifecycle()
                    .getPreStop()
                    .getExec()
                    .setCommand(command);
        }
        if (PPFramework.PP_JV_1.getDisplayName()
                .equals(baseline.getFramework()) || PPFramework.PP_JV_2.getDisplayName()
                .equals(baseline.getFramework())) {
            List<String> command = List.of("curl", "http://127.0.0.1:8081/actuator/eksshutdown", "-X", "POST");
            container.getLifecycle()
                    .getPreStop()
                    .getExec()
                    .setCommand(command);
        }
    }

    private DeploymentBaselineModel.Lifecycle generateBaselineContent(String framework) {
        if (PPFramework.PP_JV_SPRINGBOOT_2.getDisplayName()
                .equals(framework)) {
            List<String> command = List.of("curl", "http://127.0.0.1:8081/actuator/shutdown", "-X", "POST");
            DeploymentBaselineModel.ExecAction exec = DeploymentBaselineModel.ExecAction.builder()
                    .command(command)
                    .build();
            DeploymentBaselineModel.LifecycleHandler preStop = DeploymentBaselineModel.LifecycleHandler.builder()
                    .exec(exec)
                    .build();
            return DeploymentBaselineModel.Lifecycle.builder()
                    .preStop(preStop)
                    .build();
        }
        if (PPFramework.PP_JV_1.getDisplayName()
                .equals(framework) || PPFramework.PP_JV_2.getDisplayName()
                .equals(framework)) {
            List<String> command = List.of("curl", "http://127.0.0.1:8081/actuator/eksshutdown", "-X", "POST");
            DeploymentBaselineModel.ExecAction exec = DeploymentBaselineModel.ExecAction.builder()
                    .command(command)
                    .build();
            DeploymentBaselineModel.LifecycleHandler preStop = DeploymentBaselineModel.LifecycleHandler.builder()
                    .exec(exec)
                    .build();
            return DeploymentBaselineModel.Lifecycle.builder()
                    .preStop(preStop)
                    .build();
        }
        return DeploymentBaselineModel.Lifecycle.EMPTY;
    }

}
