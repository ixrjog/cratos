package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesPodVO;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtil;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;

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

    public static KubernetesPodBuilder newBuilder() {
        return new KubernetesPodBuilder();
    }

    public KubernetesPodBuilder withPod(Pod pod) {
        this.pod = pod;
        return this;
    }

    public KubernetesPodBuilder withDeployment(Deployment deployment) {
        this.deployment = deployment;
        return this;
    }

    public KubernetesPodVO.Pod build() {
        // 找出应用容器
        Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(this.deployment);
        Map<String, KubernetesPodVO.PodCondition> conditions = this.pod.getStatus()
                .getConditions()
                .stream()
                .map(e -> KubernetesPodVO.PodCondition.builder()
                        .type(e.getType())
                        .status(e.getStatus())
                        .lastTransitionTime(ConverterUtil.parse(e.getLastTransitionTime()))
                        .build())
                .collect(Collectors.toMap(KubernetesPodVO.PodCondition::getType, a -> a, (k1, k2) -> k1));
        KubernetesPodVO.PodStatus podStatus = KubernetesPodVO.PodStatus.builder()
                .hostIP(this.pod.getStatus()
                        .getHostIP())
                .podIP(this.pod.getStatus()
                        .getPodIP())
                .startTime(ConverterUtil.parse(this.pod.getStatus()
                        .getStartTime()))
                .phase(this.pod.getStatus()
                        .getPhase())
                .conditions(conditions)
                .build();
        List<KubernetesContainerVO.ContainerStatus> containerStatuses = this.pod.getStatus()
                .getContainerStatuses()
                .stream()
                .map(e -> KubernetesContainerStatusBuilder.newBuilder()
                        .withContainer(optionalContainer.orElse(null))
                        .withContainerStatus(e)
                        .build())
                .toList();
        return KubernetesPodVO.Pod.builder()
                .containerStatuses(containerStatuses)
                .metadata(ConverterUtil.toMetadata(this.pod.getMetadata()))
                .status(podStatus)
                .build();
    }

}
