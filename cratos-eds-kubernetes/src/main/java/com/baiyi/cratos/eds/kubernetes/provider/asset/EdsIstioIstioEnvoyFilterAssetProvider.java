package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioEnvoyFilterRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/13 17:20
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_ENVOY_FILTER)
public class EdsIstioIstioEnvoyFilterAssetProvider extends BaseEdsKubernetesAssetProvider<EnvoyFilter> {

    private final KubernetesIstioEnvoyFilterRepo kubernetesIstioEnvoyFilterRepo;

    public EdsIstioIstioEnvoyFilterAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                 CredentialService credentialService,
                                                 ConfigCredTemplate configCredTemplate,
                                                 EdsAssetIndexFacade edsAssetIndexFacade,
                                                 UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                 EdsInstanceProviderHolderBuilder holderBuilder,
                                                 KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                 KubernetesIstioEnvoyFilterRepo kubernetesIstioEnvoyFilterRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesIstioEnvoyFilterRepo = kubernetesIstioEnvoyFilterRepo;
    }

    @Override
    protected List<EnvoyFilter> listEntities(String namespace,
                                             ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesIstioEnvoyFilterRepo.list(instance.getConfig(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance, EdsAsset edsAsset,
            EnvoyFilter entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        return indices;
    }

}
