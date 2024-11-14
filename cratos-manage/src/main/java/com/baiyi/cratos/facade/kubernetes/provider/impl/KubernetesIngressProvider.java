package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesDeploymentAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIngressRepo;
import com.baiyi.cratos.facade.kubernetes.provider.BaseKubernetesResourceProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 11:13
 * &#064;Version 1.0
 */
@Component
public class KubernetesIngressProvider extends BaseKubernetesResourceProvider<EdsKubernetesDeploymentAssetProvider, KubernetesIngressRepo, Ingress> {

    private final KubernetesIngressRepo kubernetesIngressRepo;

    public KubernetesIngressProvider(EdsInstanceService edsInstanceService,
                                     EdsInstanceProviderHolderBuilder holderBuilder,
                                     KubernetesIngressRepo kubernetesIngressRepo) {
        super(edsInstanceService, holderBuilder);
        this.kubernetesIngressRepo = kubernetesIngressRepo;
    }

    @Override
    public String getKind() {
        return KubernetesResourceKindEnum.INGRESS.name();
    }

    @Override
    protected KubernetesIngressRepo getRepo() {
        return kubernetesIngressRepo;
    }

}