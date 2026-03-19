package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesConfigMapRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import io.fabric8.kubernetes.api.model.ConfigMap;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/27 11:20
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_CONFIG_MAP)
public class EdsKubernetesConfigMapAssetProvider extends BaseEdsKubernetesAssetProvider<ConfigMap> {

    private final KubernetesConfigMapRepo kubernetesConfigMapRepo;

    public EdsKubernetesConfigMapAssetProvider(EdsAssetProviderContext context,
                                               KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                               KubernetesConfigMapRepo kubernetesConfigMapRepo) {
        super(context, kubernetesNamespaceRepo);
        this.kubernetesConfigMapRepo = kubernetesConfigMapRepo;
    }

    @Override
    protected List<ConfigMap> listEntities(String namespace,
                                           ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesConfigMapRepo.list(instance.getConfig(), namespace);
    }

}
