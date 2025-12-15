package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesAutoscalerRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.resource.AdvancedHorizontalPodAutoscaler;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/25 下午4:11
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_ALIBABACLOUD_AUTOSCALER)
public class EdsKubernetesAlibabacloudAutoscalerAssetProvider extends BaseEdsKubernetesAssetProvider<AdvancedHorizontalPodAutoscaler> {

    private final KubernetesAutoscalerRepo kubernetesAutoscalerRepo;

    public EdsKubernetesAlibabacloudAutoscalerAssetProvider(EdsAssetService edsAssetService,
                                                            SimpleEdsFacade simpleEdsFacade,
                                                            CredentialService credentialService,
                                                            ConfigCredTemplate configCredTemplate,
                                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                            EdsInstanceProviderHolderBuilder holderBuilder,
                                                            KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                            KubernetesAutoscalerRepo kubernetesAutoscalerRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesAutoscalerRepo = kubernetesAutoscalerRepo;
    }

    @Override
    protected List<AdvancedHorizontalPodAutoscaler> listEntities(String namespace,
                                                                 ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesAutoscalerRepo.list(instance.getEdsConfigModel(), namespace)
                .stream()
                .map(Resource::item)
                .toList();
    }

}
