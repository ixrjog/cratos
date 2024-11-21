package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerStatus;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 14:00
 * &#064;Version 1.0
 */
public class KubernetesContainerStatusBuilder {

    private Container container;
    private ContainerStatus containerStatus;

    public static KubernetesContainerStatusBuilder newBuilder() {
        return new KubernetesContainerStatusBuilder();
    }

    public KubernetesContainerStatusBuilder withContainer(Container container) {
        this.container = container;
        return this;
    }

    public KubernetesContainerStatusBuilder withContainerStatus(ContainerStatus containerStatus) {
        this.containerStatus = containerStatus;
        return this;
    }

    public KubernetesContainerVO.ContainerStatus build() {
        boolean main = false;
        if (container != null) {
            main = container.getName()
                    .equals(containerStatus.getName());
        }
        return KubernetesContainerVO.ContainerStatus.builder()
                .containerID(containerStatus.getContainerID())
                .started(containerStatus.getStarted())
                .image(containerStatus.getImage())
                .imageID(containerStatus.getImageID())
                .name(containerStatus.getName())
                .restartCount(containerStatus.getRestartCount())
                .main(main)
                .build();
    }

}
