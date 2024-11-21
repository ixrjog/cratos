package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesPodVO;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtil;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 16:35
 * &#064;Version 1.0
 */
public class KubernetesDeploymentBuilder {

    private Deployment deployment;
    private List<Pod> pods;

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

    public KubernetesDeploymentVO.Deployment build() {
        KubernetesDeploymentVO.DeploymentSpec spec = KubernetesDeploymentVO.DeploymentSpec.builder()
                .replicas(this.deployment.getSpec()
                        .getReplicas())
                .build();
        List<KubernetesPodVO.Pod> podList = CollectionUtils.isEmpty(
                this.pods) ? Collections.emptyList() : this.pods.stream()
                .map(e -> KubernetesPodBuilder.newBuilder()
                        .withDeployment(this.deployment)
                        .withPod(e)
                        .build())
                .toList();
        return KubernetesDeploymentVO.Deployment.builder()
                .metadata(ConverterUtil.toMetadata(this.deployment.getMetadata()))
                .pods(podList)
                .spec(spec)
                .build();
    }

}
