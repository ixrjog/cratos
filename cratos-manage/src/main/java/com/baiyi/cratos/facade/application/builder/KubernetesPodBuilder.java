package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesPodVO;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtils;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 13:52
 * &#064;Version 1.0
 */
public class KubernetesPodBuilder {

    private Deployment deployment;
    private Pod pod;
    private EdsKubernetesConfigModel.KubernetesGrafana grafana;

    public static KubernetesPodBuilder newBuilder() {
        return new KubernetesPodBuilder();
    }

    public KubernetesPodBuilder withGrafana(EdsKubernetesConfigModel.KubernetesGrafana grafana) {
        this.grafana = grafana;
        return this;
    }

    public KubernetesPodBuilder withPod(Pod pod) {
        this.pod = pod;
        return this;
    }

    public KubernetesPodBuilder withDeployment(Deployment deployment) {
        this.deployment = deployment;
        return this;
    }

    @Schema(type = "pod.containerStatuses")
    private List<KubernetesContainerVO.ContainerStatus> makeContainerStatuses() {
        Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(this.deployment);
        return this.pod.getStatus()
                .getContainerStatuses()
                .stream()
                .map(e -> KubernetesContainerStatusBuilder.newBuilder()
                        .withContainer(optionalContainer.orElse(null))
                        .withContainerStatus(e)
                        .build())
                .toList();
    }

    @Schema(type = "pod.status.conditions")
    private Map<String, KubernetesPodVO.PodCondition> makeConditions() {
        //Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(this.deployment);
        return this.pod.getStatus()
                .getConditions()
                .stream()
                .map(e -> KubernetesPodVO.PodCondition.builder()
                        .type(e.getType())
                        .status(e.getStatus())
                        .lastTransitionTime(ConverterUtils.parse(e.getLastTransitionTime()))
                        .build())
                .collect(Collectors.toMap(KubernetesPodVO.PodCondition::getType, a -> a, (k1, k2) -> k1));
    }

    @Schema(type = "pod.status")
    private KubernetesPodVO.PodStatus makePodStatus() {
        return KubernetesPodVO.PodStatus.builder()
                .hostIP(this.pod.getStatus()
                                .getHostIP())
                .podIP(this.pod.getStatus()
                               .getPodIP())
                .startTime(ConverterUtils.parse(this.pod.getStatus()
                                                        .getStartTime()))
                .phase(this.pod.getStatus()
                               .getPhase())
                .conditions(makeConditions())
                .build();
    }

    @Schema(type = "pod.spec")
    private KubernetesPodVO.PodSpec makePodSpec() {
        return KubernetesPodVO.PodSpec.builder()
                .nodeName(this.pod.getSpec()
                                  .getNodeName())
                .build();
    }

    public KubernetesPodVO.Pod build() {
        KubernetesCommonVO.Metadata metadata =ConverterUtils.toMetadata(this.pod.getMetadata());
        KubernetesPodVO.PodExtendedValue ext = null;
        String podOverview = Optional.ofNullable(this.grafana)
                .map(EdsKubernetesConfigModel.KubernetesGrafana::getKubernetes)
                .map(EdsKubernetesConfigModel.GrafanaKubernetes::getPod)
                .map(EdsKubernetesConfigModel.GrafanaKubernetesPod::getOverview)
                .orElse(null);
        if (StringUtils.hasText(podOverview)) {
            String overview = StringFormatter.format(podOverview, metadata.getName());
            KubernetesNodeVO.GrafanaKubernetesPod pod = KubernetesNodeVO.GrafanaKubernetesPod.of(overview);
            KubernetesNodeVO.GrafanaKubernetes kubernetes = KubernetesNodeVO.GrafanaKubernetes.of(pod);
            KubernetesNodeVO.KubernetesGrafana grafana = KubernetesNodeVO.KubernetesGrafana.of(kubernetes);
            ext = KubernetesPodVO.PodExtendedValue.builder()
                    .grafana(grafana)
                    .build();
        }
        return KubernetesPodVO.Pod.builder()
                .containerStatuses(makeContainerStatuses())
                .metadata(metadata)
                .spec(makePodSpec())
                .status(makePodStatus())
                .ext(ext)
                .build();
    }

}
