package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.EdsIstioIstioEnvoyFilterAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioEnvoyFilterRepo;
import com.baiyi.cratos.facade.kubernetes.provider.BaseKubernetesResourceProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/13 17:17
 * &#064;Version 1.0
 */
@Component
public class KubernetesIstioEnvoyFilterProvider extends BaseKubernetesResourceProvider<EdsIstioIstioEnvoyFilterAssetProvider, KubernetesIstioEnvoyFilterRepo, EnvoyFilter> {

    private final KubernetesIstioEnvoyFilterRepo kubernetesIstioEnvoyFilterRepo;

    public KubernetesIstioEnvoyFilterProvider(EdsInstanceService edsInstanceService,
                                              EdsInstanceProviderHolderBuilder holderBuilder,
                                              KubernetesIstioEnvoyFilterRepo kubernetesIstioEnvoyFilterRepo) {
        super(edsInstanceService, holderBuilder);
        this.kubernetesIstioEnvoyFilterRepo = kubernetesIstioEnvoyFilterRepo;
    }

    @Override
    public String getKind() {
        return KubernetesResourceKindEnum.ENVOY_FILTER.name();
    }

    @Override
    protected KubernetesIstioEnvoyFilterRepo getRepo() {
        return kubernetesIstioEnvoyFilterRepo;
    }

}