package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioDestinationRuleRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午3:17
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_DESTINATION_RULE)
public class EdsIstioDestinationRuleAssetProvider extends BaseEdsKubernetesAssetProvider<DestinationRule> {

    private final KubernetesIstioDestinationRuleRepo kubernetesIstioDestinationRuleRepo;

    public EdsIstioDestinationRuleAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                CredentialService credentialService,
                                                ConfigCredTemplate configCredTemplate,
                                                EdsAssetIndexFacade edsAssetIndexFacade,
                                                AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                                EdsInstanceProviderHolderBuilder holderBuilder,
                                                KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                KubernetesIstioDestinationRuleRepo kubernetesIstioDestinationRuleRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesIstioDestinationRuleRepo = kubernetesIstioDestinationRuleRepo;
    }

    @Override
    protected List<DestinationRule> listEntities(String namespace,
                                                 ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesIstioDestinationRuleRepo.list(instance.getConfig(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance, EdsAsset edsAsset,
            DestinationRule entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        return indices;
    }

}
