package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.eds.kubernetes.model.AckIngressConditionsModel;
import com.baiyi.cratos.eds.kubernetes.model.EksIngressConditionsModel;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIngressRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.networking.v1.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * @Author baiyi
 * @Date 2024/3/28 11:29
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_INGRESS)
public class EdsKubernetesIngressAssetProvider extends BaseEdsKubernetesAssetProvider<Ingress> {

    private final KubernetesIngressRepo kubernetesIngressRepo;
    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    private static final String UNDEFINED_SERVICE = "Undefined Service";
    private final EdsAssetIndexService edsAssetIndexService;

    public EdsKubernetesIngressAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                             EdsInstanceProviderHolderBuilder holderBuilder,
                                             KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                             KubernetesIngressRepo kubernetesIngressRepo,
                                             EdsAssetIndexService edsAssetIndexService, TagService tagService,
                                             BusinessTagFacade businessTagFacade) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesIngressRepo = kubernetesIngressRepo;
        this.edsAssetIndexService = edsAssetIndexService;
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
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
        // alb.ingress.kubernetes.io/traffic-limit-qps
        indices.add(getEdsAssetIndexTrafficLimitQps(instance, edsAsset, entity));
        // alb.ingress.kubernetes.io/order
        indices.add(getEdsAssetIndexOrder(instance, edsAsset, entity));
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

    private EdsAssetIndex getEdsAssetIndexOrder(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Ingress entity) {
        Map<String, String> AnnotationMap = entity.getMetadata()
                .getAnnotations();
        // alb.ingress.kubernetes.io/conditions.source-ip-example
        return AnnotationMap.keySet()
                .stream()
                .filter(key -> key.equals("alb.ingress.kubernetes.io/order"))
                .map(AnnotationMap::get)
                .filter(StringUtils::hasText)
                .findFirst()
                .map(qps -> toEdsAssetIndex(edsAsset, KUBERNETES_INGRESS_ORDER, qps))
                .orElse(null);
    }

    // 适用于ACK
    private EdsAssetIndex getEdsAssetIndexTrafficLimitQps(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Ingress entity) {
        Map<String, String> AnnotationMap = entity.getMetadata()
                .getAnnotations();
        // alb.ingress.kubernetes.io/conditions.source-ip-example
        return AnnotationMap.keySet()
                .stream()
                .filter(key -> key.equals("alb.ingress.kubernetes.io/traffic-limit-qps"))
                .map(AnnotationMap::get)
                .filter(StringUtils::hasText)
                .findFirst()
                .map(qps -> toEdsAssetIndex(edsAsset, KUBERNETES_INGRESS_TRAFFIC_LIMIT_QPS, qps))
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

    @Override
    protected EdsAsset enterEntity(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance,
                                   Ingress entity) {
        EdsAsset asset = super.enterEntity(instance, entity);
        Optional.ofNullable(edsAssetIndexService.getByAssetIdAndName(asset.getId(), KUBERNETES_INGRESS_ORDER))
                .map(EdsAssetIndex::getValue)
                .ifPresent(orderValue -> {
                    Tag tag = tagService.getByTagKey(SysTagKeys.INGRESS_ORDER);
                    if (Objects.nonNull(tag)) {
                        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                                .businessId(asset.getId())
                                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                                .tagId(tag.getId())
                                .tagValue(orderValue)
                                .build();
                        businessTagFacade.saveBusinessTag(saveBusinessTag);
                    }
                });
        return asset;
    }

}
