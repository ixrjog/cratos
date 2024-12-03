package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesServiceVO;
import com.baiyi.cratos.facade.application.builder.util.ConverterUtil;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Service;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/3 13:37
 * &#064;Version 1.0
 */
public class KubernetesServiceBuilder {

    private Service service;

    public static KubernetesServiceBuilder newBuilder() {
        return new KubernetesServiceBuilder();
    }

    public KubernetesServiceBuilder withService(Service service) {
        this.service = service;
        return this;
    }

    private List<KubernetesServiceVO.ServicePort> makePorts() {
        return this.service.getSpec()
                .getPorts()
                .stream()
                .map(e -> KubernetesServiceVO.ServicePort.builder()
                        .name(e.getName())
                        .appProtocol(e.getAppProtocol())
                        .targetPort(e.getTargetPort().getIntVal())
                        .nodePort(e.getNodePort())
                        .protocol(e.getProtocol())
                        .port(e.getPort())
                        .build())
                .toList();
    }

    private KubernetesServiceVO.ServiceSpec makeServiceSpec() {
        return KubernetesServiceVO.ServiceSpec.builder()
                .ports(makePorts())
                .selector(Maps.newHashMap(this.service.getSpec()
                        .getSelector()))
                .type(this.service.getSpec().getType())
                .clusterIP(this.service.getSpec().getClusterIP())
                .build();
    }

    public KubernetesServiceVO.Service build() {
        return KubernetesServiceVO.Service.builder()
                .metadata(ConverterUtil.toMetadata(this.service.getMetadata()))
                .spec(makeServiceSpec())
                .build();
    }

}
