package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesPodVO;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.application.baseline.builder.ContainerLifecycleBuilder;
import com.baiyi.cratos.facade.application.baseline.builder.ContainerProbeBuilder;
import com.baiyi.cratos.facade.application.builder.attribute.KubernetesDeploymentAttributeBuilder;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtil;
import com.google.api.client.util.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 16:35
 * &#064;Version 1.0
 */
public class KubernetesDeploymentBuilder {

    private Deployment deployment;
    private Env env;
    private List<Pod> pods;
    private KubernetesCommonVO.KubernetesCluster kubernetesCluster;

    public static KubernetesDeploymentBuilder newBuilder() {
        return new KubernetesDeploymentBuilder();
    }

    public KubernetesDeploymentBuilder withDeployment(Deployment deployment) {
        this.deployment = deployment;
        return this;
    }

    public KubernetesDeploymentBuilder withPods(List<Pod> pods) {
        this.pods = pods;
        return this;
    }

    public KubernetesDeploymentBuilder withKubernetes(KubernetesCommonVO.KubernetesCluster kubernetesCluster) {
        this.kubernetesCluster = kubernetesCluster;
        return this;
    }

    public KubernetesDeploymentBuilder withEnv(Env env) {
        this.env = env;
        return this;
    }

    @Schema(type = "deployment.rollingUpdate")
    private KubernetesDeploymentVO.RollingUpdateDeployment makeRollingUpdate() {
        return KubernetesDeploymentVO.RollingUpdateDeployment.builder()
                .maxSurge(this.deployment.getSpec()
                        .getStrategy()
                        .getRollingUpdate()
                        .getMaxSurge()
                        .getValue()
                        .toString())
                .maxUnavailable(this.deployment.getSpec()
                        .getStrategy()
                        .getRollingUpdate()
                        .getMaxUnavailable()
                        .getValue()
                        .toString())
                .build();
    }

    @Schema(type = "deployment.strategy")
    private KubernetesDeploymentVO.DeploymentStrategy makeStrategy() {
        return this.deployment.getSpec()
                .getStrategy() == null ? KubernetesDeploymentVO.DeploymentStrategy.EMPTY : KubernetesDeploymentVO.DeploymentStrategy.builder()
                .type(this.deployment.getSpec()
                        .getStrategy()
                        .getType())
                .rollingUpdate(makeRollingUpdate())
                .build();
    }

    @Schema(type = "deployment.spec.template.spec.containers.resources")
    private KubernetesDeploymentVO.ContainerResources makeContainerResources(Container container) {
        Map<String, KubernetesDeploymentVO.Quantity> limits = Maps.newHashMap();
        container.getResources()
                .getLimits()
                .forEach((String k, Quantity v) -> limits.put(k, KubernetesDeploymentVO.Quantity.builder()
                        .amount(v.getAmount())
                        .format(v.getFormat())
                        .build()));
        Map<String, KubernetesDeploymentVO.Quantity> requests = Maps.newHashMap();
        container.getResources()
                .getRequests()
                .forEach((String k, Quantity v) -> requests.put(k, KubernetesDeploymentVO.Quantity.builder()
                        .amount(v.getAmount())
                        .format(v.getFormat())
                        .build()));
        return KubernetesDeploymentVO.ContainerResources.builder()
                .limits(limits)
                .requests(requests)
                .build();
    }

    private KubernetesDeploymentVO.TemplateSpecContainer makeTemplateSpecContainer(Container mainContainer,
                                                                                   Container container) {
        Optional<Container> optionalContainer = Optional.ofNullable(mainContainer);
        return KubernetesDeploymentVO.TemplateSpecContainer.builder()
                .name(container.getName())
                .image(container.getImage())
                .main(optionalContainer.map(main -> main.getName()
                                .equals(container.getName()))
                        .orElse(false))
                .resources(makeContainerResources(container))
                .lifecycle(ContainerLifecycleBuilder.newBuilder()
                        .withContainer(container)
                        .build())
                .livenessProbe(ContainerProbeBuilder.newBuilder()
                        .withProbe(container.getLivenessProbe())
                        .build())
                .readinessProbe(ContainerProbeBuilder.newBuilder()
                        .withProbe(container.getReadinessProbe())
                        .build())
                .startupProbe(ContainerProbeBuilder.newBuilder()
                        .withProbe(container.getStartupProbe())
                        .build())
                .build();
    }

    @Schema(type = "deployment.spec.template.spec.containers")
    private List<KubernetesDeploymentVO.TemplateSpecContainer> makeTemplateSpecContainers() {
        Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(this.deployment);
        return this.deployment.getSpec()
                .getTemplate()
                .getSpec()
                .getContainers()
                .stream()
                .map(e -> makeTemplateSpecContainer(optionalContainer.orElse(null), e))
                .sorted(Comparator.comparing(KubernetesDeploymentVO.TemplateSpecContainer::getSeq))
                .toList();
    }

    @Schema(type = "deployment.spec.template")
    private KubernetesDeploymentVO.SpecTemplate makeSpecTemplate() {
        KubernetesDeploymentVO.TemplateSpec spec = KubernetesDeploymentVO.TemplateSpec.builder()
                .containers(makeTemplateSpecContainers())
                .build();
        return KubernetesDeploymentVO.SpecTemplate.builder()
                .spec(spec)
                .build();
    }

    @Schema(type = "deployment.spec")
    private KubernetesDeploymentVO.DeploymentSpec makeSpec() {
        return KubernetesDeploymentVO.DeploymentSpec.builder()
                .replicas(this.deployment.getSpec()
                        .getReplicas())
                .strategy(makeStrategy())
                .template(makeSpecTemplate())
                .build();
    }

    private List<KubernetesPodVO.Pod> makePods() {
        return CollectionUtils.isEmpty(this.pods) ? Collections.emptyList() : this.pods.stream()
                .map(e -> KubernetesPodBuilder.newBuilder()
                        .withDeployment(this.deployment)
                        .withPod(e)
                        .build())
                .toList();
    }

    private Map<String, String> makeAttributes() {
        return KubernetesDeploymentAttributeBuilder.newBuilder()
                .withDeployment(this.deployment)
                .build();
    }

    public KubernetesDeploymentVO.Deployment build() {
        return KubernetesDeploymentVO.Deployment.builder()
                .kubernetesCluster(this.kubernetesCluster)
                .metadata(ConverterUtil.toMetadata(this.deployment.getMetadata()))
                .pods(makePods())
                .spec(makeSpec())
                .attributes(makeAttributes())
                .env(env)
                .build();
    }

}
