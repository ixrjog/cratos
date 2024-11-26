package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.asset.EdsIstioVirtualServiceAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioVirtualServiceRepo;
import com.baiyi.cratos.facade.kubernetes.provider.BaseKubernetesResourceProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/13 16:41
 * &#064;Version 1.0
 */
@Component
public class KubernetesIstioVirtualServiceProvider extends BaseKubernetesResourceProvider<EdsIstioVirtualServiceAssetProvider, KubernetesIstioVirtualServiceRepo, VirtualService> {

    private final KubernetesIstioVirtualServiceRepo kubernetesIstioVirtualServiceRepo;

    public KubernetesIstioVirtualServiceProvider(EdsInstanceService edsInstanceService,
                                                 EdsInstanceProviderHolderBuilder holderBuilder,
                                                 KubernetesIstioVirtualServiceRepo kubernetesIstioVirtualServiceRepo) {
        super(edsInstanceService, holderBuilder);
        this.kubernetesIstioVirtualServiceRepo = kubernetesIstioVirtualServiceRepo;
    }

    @Override
    public String getKind() {
        return KubernetesResourceKindEnum.VIRTUAL_SERVICE.name();
    }

    @Override
    protected KubernetesIstioVirtualServiceRepo getRepo() {
        return kubernetesIstioVirtualServiceRepo;
    }

}
