package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.eds.kubernetes.model.AckIngressConditionsModel;
import com.baiyi.cratos.eds.kubernetes.model.EksIngressConditionsModel;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesIngressRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.networking.v1.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * @Author baiyi
 * @Date 2024/3/28 11:29
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_INGRESS)
public class EdsKubernetesIngressAssetProvider extends BaseEdsKubernetesAssetProvider<Ingress> {

    private final KubernetesIngressRepo kubernetesIngressRepo;

    private static final String UNDEFINED_SERVICE = "Undefined Service";

    public EdsKubernetesIngressAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                             KubernetesIngressRepo kubernetesIngressRepo,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                kubernetesNamespaceRepo, updateBusinessFromAssetHandler);
        this.kubernetesIngressRepo = kubernetesIngressRepo;
    }

    @Override
    protected List<Ingress> listEntities(String namespace,
                                         ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesIngressRepo.list(instance.getEdsConfigModel(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Ingress entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        Optional<List<IngressRule>> optionalIngressRules = Optional.of(entity)
                .map(Ingress::getSpec)
                .map(IngressSpec::getRules);
        // rules
        optionalIngressRules.ifPresent(
                ingressRules -> indices.addAll(getEdsAssetIndexFromRules(edsAsset, ingressRules)));
        // loadBalancer
        indices.add(getEdsAssetIndexOfLoadBalancer(edsAsset, entity));
        // namespace
        indices.add(toEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        // 注解
        indices.add(getEdsAssetIndexSourceIP(instance, edsAsset, entity));
        return indices;
    }

    private EdsAssetIndex getEdsAssetIndexSourceIP(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Ingress entity) {
        Map<String, String> AnnotationMap = entity.getMetadata()
                .getAnnotations();
        // True EKS
        boolean providerType = KubernetesProvidersEnum.AMAZON_EKS.getDisplayName()
                .equals(instance.getEdsConfigModel()
                        .getProvider());
        // alb.ingress.kubernetes.io/conditions.source-ip-example
        return AnnotationMap.keySet()
                .stream()
                .filter(key -> key.startsWith("alb.ingress.kubernetes.io/conditions."))
                .map(key -> providerType ? EksIngressConditionsModel.getSourceIP(
                        AnnotationMap.get(key)) : AckIngressConditionsModel.getSourceIP(AnnotationMap.get(key)))
                .filter(StringUtils::hasText)
                .findFirst()
                .map(sourceIP -> toEdsAssetIndex(edsAsset, KUBERNETES_INGRESS_SOURCE_IP, sourceIP))
                .orElse(null);
    }

    private EdsAssetIndex getEdsAssetIndexOfLoadBalancer(EdsAsset edsAsset, Ingress entity) {
        Optional<List<IngressLoadBalancerIngress>> optionalIngressLoadBalancerIngresses = Optional.of(entity)
                .map(Ingress::getStatus)
                .map(IngressStatus::getLoadBalancer)
                .map(IngressLoadBalancerStatus::getIngress);
        if (optionalIngressLoadBalancerIngresses.isPresent() && !CollectionUtils.isEmpty(
                optionalIngressLoadBalancerIngresses.get())) {
            IngressLoadBalancerIngress ingressLoadBalancerIngress = optionalIngressLoadBalancerIngresses.get()
                    .getFirst();
            return toEdsAssetIndex(edsAsset, KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME,
                    ingressLoadBalancerIngress.getHostname());
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
                        indices.add(
                                toEdsAssetIndex(edsAsset, StringFormatter.arrayFormat("{} -> {}", host, path.getPath()),
                                        service));
                    });
        });
        return indices;
    }

}
