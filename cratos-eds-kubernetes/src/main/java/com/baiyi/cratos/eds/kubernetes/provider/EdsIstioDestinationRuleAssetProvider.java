package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.IstioDestinationRuleRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.kubernetes.api.model.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午3:17
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_DESTINATION_RULE)
public class EdsIstioDestinationRuleAssetProvider extends BaseEdsKubernetesAssetProvider<DestinationRule> {

    private final KubernetesNamespaceRepo kubernetesNamespaceRepo;

    public EdsIstioDestinationRuleAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                               CredentialService credentialService,
                                               ConfigCredTemplate configCredTemplate,
                                               EdsAssetIndexFacade edsAssetIndexFacade,
                                               KubernetesNamespaceRepo kubernetesNamespaceRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.kubernetesNamespaceRepo = kubernetesNamespaceRepo;
    }

    @Override
    protected List<DestinationRule> listEntities(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        List<Namespace> namespaces = kubernetesNamespaceRepo.list(instance.getEdsConfigModel());
        List<DestinationRule> entities = Lists.newArrayList();
        namespaces.forEach(e -> entities.addAll(IstioDestinationRuleRepo.list(instance.getEdsConfigModel(),
                e.getMetadata()
                        .getName())));
        return entities;
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            DestinationRule entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, "namespace", getNamespace(entity)));
        return indices;
    }

}

