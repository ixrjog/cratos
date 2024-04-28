package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesIngressRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.networking.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/28 11:29
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_INGRESS)
public class EdsKubernetesIngressAssetProvider extends BaseEdsKubernetesAssetProvider<Ingress> {

    private final KubernetesNamespaceRepo kubernetesNamespaceRepo;

    private final KubernetesIngressRepo kubernetesIngressRepo;

    private static final String UNDEFINED_SERVICE = "Undefined Service";

    public static final String LB_INGRESS_HOSTNAME = "loadBalancer.ingress.hostname";

    @Override
    protected List<Ingress> listEntities(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        List<Namespace> namespaces = kubernetesNamespaceRepo.list(instance.getEdsConfigModel());
        List<Ingress> entities = Lists.newArrayList();
        namespaces.forEach(e -> entities.addAll(kubernetesIngressRepo.list(instance.getEdsConfigModel(), e.getMetadata()
                .getName())));
        return entities;
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance,
                                  Ingress entity) {
        final String namespace = entity.getMetadata()
                .getNamespace();
        final String name = entity.getMetadata()
                .getName();
        final String assetId = getAssetId(namespace, name);

        return newEdsAssetBuilder(instance, entity).assetIdOf(assetId)
                .nameOf(name)
                .kindOf(entity.getKind())
                .createdTimeOf(toUTCDate(entity.getMetadata()
                        .getCreationTimestamp()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(EdsAsset edsAsset, Ingress entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        Optional<List<IngressRule>> optionalIngressRules = Optional.of(entity)
                .map(Ingress::getSpec)
                .map(IngressSpec::getRules);
        // rules
        optionalIngressRules.ifPresent(ingressRules -> indices.addAll(getEdsAssetIndexFromRules(edsAsset, ingressRules)));
        // loadBalancer
        indices.add(getEdsAssetIndexOfLoadBalancer(edsAsset, entity));
        return indices;
    }

    private EdsAssetIndex getEdsAssetIndexOfLoadBalancer(EdsAsset edsAsset, Ingress entity) {
        Optional<List<IngressLoadBalancerIngress>> optionalIngressLoadBalancerIngresses = Optional.of(entity)
                .map(Ingress::getStatus)
                .map(IngressStatus::getLoadBalancer)
                .map(IngressLoadBalancerStatus::getIngress);
        if (optionalIngressLoadBalancerIngresses.isPresent()) {
            IngressLoadBalancerIngress ingressLoadBalancerIngress = optionalIngressLoadBalancerIngresses.get()
                    .get(0);
            return EdsAssetIndex.builder()
                    .instanceId(edsAsset.getInstanceId())
                    .assetId(edsAsset.getId())
                    .name(LB_INGRESS_HOSTNAME)
                    .value(ingressLoadBalancerIngress.getHostname())
                    .build();
        }
        return null;
    }

    private List<EdsAssetIndex> getEdsAssetIndexFromRules(EdsAsset edsAsset, List<IngressRule> ingressRules) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        ingressRules.forEach(ingressRule -> {
            String host = ingressRule.getHost();
            ingressRule.getHttp()
                    .getPaths()
                    .forEach(path -> {
                        final String service = Optional.of(path)
                                .map(HTTPIngressPath::getBackend)
                                .map(IngressBackend::getService)
                                .map(IngressServiceBackend::getName)
                                .orElse(UNDEFINED_SERVICE);
                        EdsAssetIndex index = EdsAssetIndex.builder()
                                .instanceId(edsAsset.getInstanceId())
                                .assetId(edsAsset.getId())
                                .name(StringFormatter.arrayFormat("{} -> {}", host, path.getPath()))
                                .value(service)
                                .build();
                        indices.add(index);
                    });
        });
        return indices;
    }

    private Date toUTCDate(String time) {
        return com.baiyi.cratos.common.util.TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

}
