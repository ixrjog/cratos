package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesDeploymentAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.impl.KubernetesServiceRepo;
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
public class KubernetesServiceProvider extends BaseKubernetesResourceProvider<EdsKubernetesDeploymentAssetProvider, Service> {

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
    protected  Service create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        Service resource = null;
        try {
            resource = kubernetesServiceRepo.find(kubernetes, content);
        } catch (Exception ignored) {
        }
        return resource != null ? resource : kubernetesServiceRepo.create(kubernetes, content);
    }

}
