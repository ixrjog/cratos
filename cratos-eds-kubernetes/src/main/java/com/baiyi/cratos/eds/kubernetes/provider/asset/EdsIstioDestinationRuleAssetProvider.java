package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioDestinationRuleRepo;
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

    public EdsIstioDestinationRuleAssetProvider(EdsAssetProviderContext context,
                                                KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                KubernetesIstioDestinationRuleRepo kubernetesIstioDestinationRuleRepo) {
        super(context, kubernetesNamespaceRepo);
        this.kubernetesIstioDestinationRuleRepo = kubernetesIstioDestinationRuleRepo;
    }


    @Override
    protected List<DestinationRule> listEntities(String namespace,
                                                 ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesIstioDestinationRuleRepo.list(instance.getConfig(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance,
                                               EdsAsset edsAsset, DestinationRule entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        return indices;
    }

}
