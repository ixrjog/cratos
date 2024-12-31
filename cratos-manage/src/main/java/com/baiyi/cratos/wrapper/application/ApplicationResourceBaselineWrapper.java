package com.baiyi.cratos.wrapper.application;

import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;
import com.baiyi.cratos.facade.application.baseline.mode.DeploymentBaselineModel;
import com.baiyi.cratos.service.ApplicationResourceBaselineMemberService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 17:41
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceBaselineWrapper extends BaseDataTableConverter<ApplicationResourceBaselineVO.ResourceBaseline, ApplicationResourceBaseline> implements IBaseWrapper<ApplicationResourceBaselineVO.ResourceBaseline> {

    private final ApplicationResourceBaselineMemberService baselineMemberService;

    @Override
    public void wrap(ApplicationResourceBaselineVO.ResourceBaseline vo) {
        vo.setContainer(buildContainer(vo));
    }

    private ApplicationResourceBaselineVO.Container buildContainer(ApplicationResourceBaselineVO.ResourceBaseline vo) {
        List<ApplicationResourceBaselineMember> members = baselineMemberService.queryByBaselineId(vo.getId());
        Map<String, ApplicationResourceBaselineMember> memberMap = members.stream()
                .collect(Collectors.toMap(ApplicationResourceBaselineMember::getBaselineType, a -> a, (k1, k2) -> k1));
        return ApplicationResourceBaselineVO.Container.builder()
                .startupProbe(toProbe(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_STARTUP_PROBE.name())))
                .livenessProbe(toProbe(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_LIVENESS_PROBE.name())))
                .readinessProbe(toProbe(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_READINESS_PROBE.name())))
                .lifecycle(toLifecycle(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_LIFECYCLE.name())))
                .build();
    }

    private ApplicationResourceBaselineVO.Lifecycle toLifecycle(ApplicationResourceBaselineMember lifecycleMember) {
        if (lifecycleMember == null) {
            return ApplicationResourceBaselineVO.Lifecycle.EMPTY;
        }
        DeploymentBaselineModel.Lifecycle lifecycleMO = DeploymentBaselineModel.lifecycleLoadAs(
                lifecycleMember.getContent());
        String preStopExecCommand = Joiner.on(" ")
                .skipNulls()
                .join(lifecycleMO.getPreStop()
                        .getExec()
                        .getCommand());
        DeploymentBaselineModel.Lifecycle baselineLifecycleMO = DeploymentBaselineModel.lifecycleLoadAs(
                lifecycleMember.getBaselineContent());
        String baselinePreStopExecCommand = Joiner.on(" ")
                .skipNulls()
                .join(lifecycleMO.getPreStop()
                        .getExec()
                        .getCommand());
        ApplicationResourceBaselineVO.Lifecycle baseline = ApplicationResourceBaselineVO.Lifecycle.builder()
                .preStopExecCommand(baselinePreStopExecCommand)
                .content(lifecycleMember.getBaselineContent())
                .build();
        return ApplicationResourceBaselineVO.Lifecycle.builder()
                .preStopExecCommand(preStopExecCommand)
                .standard(lifecycleMember.getStandard())
                .content(lifecycleMember.getContent())
                .baseline(baseline)
                .build();
    }

    private ApplicationResourceBaselineVO.Probe toProbe(ApplicationResourceBaselineMember probeMember) {
        if (probeMember == null) {
            return ApplicationResourceBaselineVO.Probe.EMPTY;
        }
        DeploymentBaselineModel.Probe probeMO = DeploymentBaselineModel.probeLoadAs(probeMember.getContent());
        DeploymentBaselineModel.Probe baselineProbeMO = DeploymentBaselineModel.probeLoadAs(
                probeMember.getBaselineContent());
        ApplicationResourceBaselineVO.Probe baseline = ApplicationResourceBaselineVO.Probe.builder()
                .path(baselineProbeMO.getHttpGet()
                        .getPath())
                .port(baselineProbeMO.getHttpGet()
                        .getPort())
                .content(probeMember.getBaselineContent())
                .build();
        return ApplicationResourceBaselineVO.Probe.builder()
                .path(probeMO.getHttpGet()
                        .getPath())
                .port(probeMO.getHttpGet()
                        .getPort())
                .baseline(baseline)
                .standard(probeMember.getStandard())
                .content(probeMember.getContent())
                .build();
    }

}