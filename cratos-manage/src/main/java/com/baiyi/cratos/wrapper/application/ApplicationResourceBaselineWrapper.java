package com.baiyi.cratos.wrapper.application;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineRedeployingFacade;
import com.baiyi.cratos.service.ApplicationResourceBaselineMemberService;
import com.baiyi.cratos.wrapper.application.converter.BaselineConverter;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.google.api.client.util.Lists;
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
    private final ApplicationResourceBaselineRedeployingFacade deploymentRedeployFacade;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.ENV})
    public void wrap(ApplicationResourceBaselineVO.ResourceBaseline vo) {
        vo.setIsDeploying(deploymentRedeployFacade.isRedeploying(vo.getId()));
        vo.setContainer(buildContainer(vo));
    }

    private ApplicationResourceBaselineVO.Container buildContainer(ApplicationResourceBaselineVO.ResourceBaseline vo) {
        List<ApplicationResourceBaselineMember> members = baselineMemberService.queryByBaselineId(vo.getId());
        Map<String, ApplicationResourceBaselineMember> memberMap = members.stream()
                .collect(Collectors.toMap(ApplicationResourceBaselineMember::getBaselineType, a -> a, (k1, k2) -> k1));
        List<ApplicationResourceBaselineVO.EnvVar> env = Lists.newArrayList();
        ApplicationResourceBaselineVO.EnvVar springOptsEnvVar = toEnv(
                memberMap.get(ResourceBaselineTypeEnum.CONTAINER_ENV_SPRING_OPTS.name()));
        if (springOptsEnvVar != null) {
            env.add(springOptsEnvVar);
        }
        return ApplicationResourceBaselineVO.Container.builder()
                .startupProbe(toProbe(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_STARTUP_PROBE.name())))
                .livenessProbe(toProbe(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_LIVENESS_PROBE.name())))
                .readinessProbe(toProbe(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_READINESS_PROBE.name())))
                .lifecycle(toLifecycle(memberMap.get(ResourceBaselineTypeEnum.CONTAINER_LIFECYCLE.name())))
                .env(env)
                .build();
    }

    private ApplicationResourceBaselineVO.EnvVar toEnv(ApplicationResourceBaselineMember envMember) {
        if (envMember == null) {
            return ApplicationResourceBaselineVO.EnvVar.builder()
                    .build();
        }
        ApplicationResourceBaselineVO.EnvVar baselineEnvVar = BaselineConverter.toEnv(envMember.getBaselineContent());
        ApplicationResourceBaselineVO.EnvVar envVar = BaselineConverter.toEnv(envMember.getContent());
        envVar.setBaseline(BaselineConverter.toEnv(envMember.getBaselineContent()));
        envVar.setStandard(envMember.getStandard());
        return envVar;
    }

    private ApplicationResourceBaselineVO.Lifecycle toLifecycle(ApplicationResourceBaselineMember lifecycleMember) {
        if (lifecycleMember == null) {
            return ApplicationResourceBaselineVO.Lifecycle.EMPTY;
        }
        ApplicationResourceBaselineVO.Lifecycle lifecycle = BaselineConverter.toLifecycle(lifecycleMember.getContent());
        lifecycle.setBaseline(BaselineConverter.toLifecycle(lifecycleMember.getBaselineContent()));
        lifecycle.setStandard(lifecycleMember.getStandard());
        return lifecycle;
    }

    private ApplicationResourceBaselineVO.Probe toProbe(ApplicationResourceBaselineMember probeMember) {
        if (probeMember == null) {
            return ApplicationResourceBaselineVO.Probe.EMPTY;
        }
        ApplicationResourceBaselineVO.Probe probe = BaselineConverter.toProbe(probeMember.getContent());
        probe.setBaseline(BaselineConverter.toProbe(probeMember.getBaselineContent()));
        probe.setStandard(probeMember.getStandard());
        return probe;
    }

}