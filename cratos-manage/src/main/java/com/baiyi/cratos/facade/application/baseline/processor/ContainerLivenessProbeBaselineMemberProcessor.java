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
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 16:26
 * &#064;Version 1.0
 */
@Component
public class ContainerLivenessProbeBaselineMemberProcessor extends BaseContainerBaselineMemberProcessor {

    public ContainerLivenessProbeBaselineMemberProcessor(
            ApplicationResourceBaselineMemberService baselineMemberService) {
        super(baselineMemberService);
    }

    @Override
    public ResourceBaselineTypeEnum getType() {
        return ResourceBaselineTypeEnum.CONTAINER_LIVENESS_PROBE;
    }

    @Override
    public void saveMember(ApplicationResourceBaseline baseline, Container container) {
        DeploymentBaselineModel.Probe livenessProbe = DeploymentBaselineConverter.to(Optional.of(container)
                .map(Container::getLivenessProbe)
                .orElse(null));
        DeploymentBaselineModel.Probe baselineLivenessProbe = generateBaselineContent(baseline.getFramework());
        ApplicationResourceBaselineMember livenessProbeMember = ApplicationResourceBaselineMember.builder()
                .baselineId(baseline.getId())
                .baselineType(getType().name())
                .name(getType().getDisplayName())
                .applicationName(baseline.getApplicationName())
                .namespace(baseline.getNamespace())
                .content(livenessProbe.dump())
                .baselineContent(baselineLivenessProbe.dump())
                .standard(DeploymentBaselineModel.Probe.validate(livenessProbe, baselineLivenessProbe))
                .build();
        save(livenessProbeMember);
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
                    .path("/actuator/health/liveness")
                    .scheme("HTTP")
                    .build();
            return DeploymentBaselineModel.Probe.builder()
                    .httpGet(httpGet)
                    .build();
        }
        return DeploymentBaselineModel.Probe.EMPTY;
    }

}