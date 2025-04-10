package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.asset.EdsKubernetesDeploymentAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesServiceRepo;
import com.baiyi.cratos.facade.kubernetes.provider.BaseKubernetesResourceProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 11:11
 * &#064;Version 1.0
 */
@Component
public class KubernetesServiceProvider extends BaseKubernetesResourceProvider<EdsKubernetesDeploymentAssetProvider, KubernetesServiceRepo, Service> {

    private final KubernetesServiceRepo kubernetesServiceRepo;

    public KubernetesServiceProvider(EdsInstanceService edsInstanceService,
                                     EdsInstanceProviderHolderBuilder holderBuilder,
                                     KubernetesServiceRepo kubernetesServiceRepo) {
        super(edsInstanceService, holderBuilder);
        this.kubernetesServiceRepo = kubernetesServiceRepo;
    }

    @Override
    public String getKind() {
        return KubernetesResourceKindEnum.SERVICE.name();
    }

    @Override
    protected KubernetesServiceRepo getRepo() {
        return kubernetesServiceRepo;
    }

}
