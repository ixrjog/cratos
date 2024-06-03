package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.api.model.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午5:28
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_NAMESPACE)
public class EdsKubernetesNamespaceAssetProvider extends BaseEdsKubernetesAssetProvider<Namespace> {

    private final KubernetesNamespaceRepo kubernetesNamespaceRepo;

    public EdsKubernetesNamespaceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                               CredentialService credentialService,
                                               ConfigCredTemplate configCredTemplate,
                                               EdsAssetIndexFacade edsAssetIndexFacade,
                                               KubernetesNamespaceRepo kubernetesNamespaceRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                kubernetesNamespaceRepo);
        this.kubernetesNamespaceRepo = kubernetesNamespaceRepo;
    }

    @Override
    protected List<Namespace> listEntities(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesNamespaceRepo.list(instance.getEdsConfigModel());
    }

    @Override
    protected List<Namespace> listEntities(String namespace,
                                           ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        // Not supported
        return List.of();
    }

}
