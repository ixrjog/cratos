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
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 16:58
 * &#064;Version 1.0
 */
@Component
public class ContainerReadinessProbeBaselineMemberProcessor extends BaseContainerBaselineMemberProcessor {

    public ContainerReadinessProbeBaselineMemberProcessor(
            ApplicationResourceBaselineMemberService baselineMemberService) {
        super(baselineMemberService);
    }

    @Override
    public ResourceBaselineTypeEnum getType() {
        return ResourceBaselineTypeEnum.CONTAINER_READINESS_PROBE;
    }

    @Override
    public void saveMember(ApplicationResourceBaseline baseline, Container container) {
        DeploymentBaselineModel.Probe readinessProbe = DeploymentBaselineConverter.to(Optional.of(container)
                .map(Container::getReadinessProbe)
                .orElse(null));
        DeploymentBaselineModel.Probe baselineReadinessProbe = generateBaselineContent(baseline.getFramework());
        ApplicationResourceBaselineMember readinessProbeMember = ApplicationResourceBaselineMember.builder()
                .baselineId(baseline.getId())
                .baselineType(getType().name())
                .name(getType().getDisplayName())
                .applicationName(baseline.getApplicationName())
                .namespace(baseline.getNamespace())
                .content(readinessProbe.dump())
                .baselineContent(baselineReadinessProbe.dump())
                .standard(DeploymentBaselineModel.Probe.validate(readinessProbe, baselineReadinessProbe))
                .build();
        save(readinessProbeMember);
    }

    @Override
    public void mergeToBaseline(ApplicationResourceBaseline baseline, ApplicationResourceBaselineMember baselineMember,
                                Deployment deployment, Container container) {
        container.getReadinessProbe().getHttpGet().setPort(new IntOrString(8081));
        if (PPFramework.PP_JV_1.getDisplayName()
                .equals(baseline.getFramework())) {

            container.getReadinessProbe().getHttpGet().setPath("/actuator/health");
        }
        if (PPFramework.PP_JV_SPRINGBOOT_2.getDisplayName()
                .equals(baseline.getFramework()) || PPFramework.PP_JV_2.getDisplayName()
                .equals(baseline.getFramework())) {
            container.getReadinessProbe().getHttpGet().setPath("/actuator/health/readiness");
        }
    }

    private DeploymentBaselineModel.Probe generateBaselineContent(String framework) {
        if (PPFramework.PP_JV_1.getDisplayName()
                .equals(framework)) {
            DeploymentBaselineModel.HTTPGetAction httpGet = DeploymentBaselineModel.HTTPGetAction.builder()
                    .port(8081)
                    .path("/actuator/health")
                    .scheme("HTTP")
                    .build();
            return DeploymentBaselineModel.Probe.builder()
                    .httpGet(httpGet)
                    .build();
        }
        if (PPFramework.PP_JV_SPRINGBOOT_2.getDisplayName()
                .equals(framework) || PPFramework.PP_JV_2.getDisplayName()
                .equals(framework)) {
            DeploymentBaselineModel.HTTPGetAction httpGet = DeploymentBaselineModel.HTTPGetAction.builder()
                    .port(8081)
                    .path("/actuator/health/readiness")
                    .scheme("HTTP")
                    .build();
            return DeploymentBaselineModel.Probe.builder()
                    .httpGet(httpGet)
                    .build();
        }
        return DeploymentBaselineModel.Probe.EMPTY;
    }

}