package com.baiyi.cratos.facade.application.baseline.processor;

import com.baiyi.cratos.common.enums.PPFramework;
import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.facade.application.baseline.mode.DeploymentBaselineModel;
import com.baiyi.cratos.facade.application.baseline.processor.base.BaseContainerBaselineMemberProcessor;
import com.baiyi.cratos.service.ApplicationResourceBaselineMemberService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/31 15:55
 * &#064;Version 1.0
 */
@Component
public class ContainerEnvSpringOptsBaselineMemberProcessor extends BaseContainerBaselineMemberProcessor {

    public ContainerEnvSpringOptsBaselineMemberProcessor(
            ApplicationResourceBaselineMemberService baselineMemberService) {
        super(baselineMemberService);
    }

    @Override
    public ResourceBaselineTypeEnum getType() {
        return ResourceBaselineTypeEnum.CONTAINER_ENV_SPRING_OPTS;
    }

    @Override
    public void saveMember(ApplicationResourceBaseline baseline, Container container) {
        Optional<EnvVar> optionalEnvVar = container.getEnv()
                .stream()
                .filter(e -> "SPRING_OPTS".equals(e.getName()))
                .findFirst();
        // 不处理
        if (optionalEnvVar.isEmpty()) {
            return;
        }
        EnvVar env = optionalEnvVar.get();
        DeploymentBaselineModel.ConfigMapKeySelector configMapKeyRef = DeploymentBaselineModel.ConfigMapKeySelector.builder()
                .key(env.getValueFrom()
                        .getConfigMapKeyRef()
                        .getKey())
                .name(env.getValueFrom()
                        .getConfigMapKeyRef()
                        .getName())
                .optional(env.getValueFrom()
                        .getConfigMapKeyRef()
                        .getOptional())
                .build();
        DeploymentBaselineModel.EnvVarSource valueFrom = DeploymentBaselineModel.EnvVarSource.builder()
                .configMapKeyRef(configMapKeyRef)
                .build();
        DeploymentBaselineModel.EnvVar envVar = DeploymentBaselineModel.EnvVar.builder()
                .name(env.getName())
                .value(env.getValue())
                .valueFrom(valueFrom)
                .build();
        DeploymentBaselineModel.EnvVar baselineSpringOpts = generateBaselineContent(baseline.getFramework(),env);
        ApplicationResourceBaselineMember springOptsMember = ApplicationResourceBaselineMember.builder()
                .baselineId(baseline.getId())
                .baselineType(getType().name())
                .name(getType().getDisplayName())
                .applicationName(baseline.getApplicationName())
                .namespace(baseline.getNamespace())
                .content(envVar.dump())
                .baselineContent(baselineSpringOpts.dump())
                .standard(DeploymentBaselineModel.EnvVar.validate(envVar, baselineSpringOpts))
                .build();
        save(springOptsMember);
    }

    private DeploymentBaselineModel.EnvVar generateBaselineContent(String framework, EnvVar env) {
        if ("java-options".equals(env.getValueFrom()
                .getConfigMapKeyRef()
                .getName())) {
            return generateAliyunBaselineContent(framework, env);
        }
        if ("java-options-aws".equals(env.getValueFrom()
                .getConfigMapKeyRef()
                .getName())) {
            return generateAwsBaselineContent(framework, env);
        }
        return DeploymentBaselineModel.EnvVar.EMPTY;
    }


    private DeploymentBaselineModel.EnvVar generateAliyunBaselineContent(String framework, EnvVar env) {
        String key = "";
        if (PPFramework.PP_JV_SPRINGBOOT_2.getDisplayName()
                .equals(framework)) {
            key = PPFramework.PP_JV_SPRINGBOOT_2.name();
        }
        if (PPFramework.PP_JV_1.getDisplayName()
                .equals(framework)) {
            key = "ALIYUN_" + PPFramework.PP_JV_1.name();
        }
        if (PPFramework.PP_JV_2.getDisplayName()
                .equals(framework)) {
            key = PPFramework.PP_JV_2.name();
        }
        DeploymentBaselineModel.ConfigMapKeySelector configMapKeyRef = DeploymentBaselineModel.ConfigMapKeySelector.builder()
                .key(key)
                .name("java-options-common")
                .build();
        DeploymentBaselineModel.EnvVarSource valueFrom = DeploymentBaselineModel.EnvVarSource.builder()
                .configMapKeyRef(configMapKeyRef)
                .build();
        return DeploymentBaselineModel.EnvVar.builder()
                .name(env.getName())
                .value(env.getValue())
                .valueFrom(valueFrom)
                .build();
    }

    private DeploymentBaselineModel.EnvVar generateAwsBaselineContent(String framework, EnvVar env) {
        String key = "";
        if (PPFramework.PP_JV_SPRINGBOOT_2.getDisplayName()
                .equals(framework)) {
            key = PPFramework.PP_JV_SPRINGBOOT_2.name();
        }
        if (PPFramework.PP_JV_1.getDisplayName()
                .equals(framework)) {
            key = "AWS_" + PPFramework.PP_JV_1.name();
        }
        if (PPFramework.PP_JV_2.getDisplayName()
                .equals(framework)) {
            key = PPFramework.PP_JV_2.name();
        }
        DeploymentBaselineModel.ConfigMapKeySelector configMapKeyRef = DeploymentBaselineModel.ConfigMapKeySelector.builder()
                .key(key)
                .name("java-options-common")
                .build();
        DeploymentBaselineModel.EnvVarSource valueFrom = DeploymentBaselineModel.EnvVarSource.builder()
                .configMapKeyRef(configMapKeyRef)
                .build();
        return DeploymentBaselineModel.EnvVar.builder()
                .name(env.getName())
                .value(env.getValue())
                .valueFrom(valueFrom)
                .build();
    }

}
