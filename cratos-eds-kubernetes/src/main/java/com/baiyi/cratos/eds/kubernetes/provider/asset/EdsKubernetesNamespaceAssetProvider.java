package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import io.fabric8.kubernetes.api.model.Namespace;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午5:28
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_NAMESPACE)
public class EdsKubernetesNamespaceAssetProvider extends BaseEdsKubernetesAssetProvider<Namespace> {


    public EdsKubernetesNamespaceAssetProvider(EdsAssetProviderContext context,
                                               KubernetesNamespaceRepo kubernetesNamespaceRepo) {
        super(context, kubernetesNamespaceRepo);
    }

    @Override
    protected List<Namespace> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesNamespaceRepo.list(instance.getConfig());
    }

    @Override
    protected List<Namespace> listEntities(String namespace,
                                           ExternalDataSourceInstance<EdsConfigs.Kubernetes> instance) throws EdsQueryEntitiesException {
        // Not supported
        return List.of();
    }

}
