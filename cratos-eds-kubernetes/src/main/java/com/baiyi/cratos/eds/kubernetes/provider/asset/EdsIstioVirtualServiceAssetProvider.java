package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
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
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioVirtualServiceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午3:02
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_VIRTUAL_SERVICE)
public class EdsIstioVirtualServiceAssetProvider extends BaseEdsKubernetesAssetProvider<VirtualService> {

    private final KubernetesIstioVirtualServiceRepo kubernetesIstioVirtualServiceRepo;

    public EdsIstioVirtualServiceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                               CredentialService credentialService,
                                               ConfigCredTemplate configCredTemplate,
                                               EdsAssetIndexFacade edsAssetIndexFacade,
                                               UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                               EdsInstanceProviderHolderBuilder holderBuilder,
                                               KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                               KubernetesIstioVirtualServiceRepo kubernetesIstioVirtualServiceRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesIstioVirtualServiceRepo = kubernetesIstioVirtualServiceRepo;
    }

    @Override
    protected List<VirtualService> listEntities(String namespace,
                                                ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesIstioVirtualServiceRepo.list(instance.getEdsConfigModel(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            VirtualService entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        return indices;
    }

}
