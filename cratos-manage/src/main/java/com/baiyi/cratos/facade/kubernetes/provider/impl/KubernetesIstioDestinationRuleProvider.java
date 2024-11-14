package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.EdsIstioDestinationRuleAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIstioDestinationRuleRepo;
import com.baiyi.cratos.facade.kubernetes.provider.BaseKubernetesResourceProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/13 16:49
 * &#064;Version 1.0
 */
@Component
public class KubernetesIstioDestinationRuleProvider extends BaseKubernetesResourceProvider<EdsIstioDestinationRuleAssetProvider, KubernetesIstioDestinationRuleRepo, DestinationRule> {

    private final KubernetesIstioDestinationRuleRepo kubernetesIstioDestinationRuleRepo;

    public KubernetesIstioDestinationRuleProvider(EdsInstanceService edsInstanceService,
                                                  EdsInstanceProviderHolderBuilder holderBuilder,
                                                  KubernetesIstioDestinationRuleRepo kubernetesIstioDestinationRuleRepo) {
        super(edsInstanceService, holderBuilder);
        this.kubernetesIstioDestinationRuleRepo = kubernetesIstioDestinationRuleRepo;
    }

    @Override
    public String getKind() {
        return KubernetesResourceKindEnum.DESTINATION_RULE.name();
    }

    @Override
    protected KubernetesIstioDestinationRuleRepo getRepo() {
        return kubernetesIstioDestinationRuleRepo;
    }

}
