package com.baiyi.cratos.facade.kubernetes.provider.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesDeploymentAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.facade.kubernetes.provider.BaseKubernetesResourceProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 14:17
 * &#064;Version 1.0
 */
@Component
public class KubernetesDeploymentProvider extends BaseKubernetesResourceProvider<EdsKubernetesDeploymentAssetProvider, Deployment> {

    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;

    public KubernetesDeploymentProvider(EdsInstanceService edsInstanceService,
                                        EdsInstanceProviderHolderBuilder holderBuilder,
                                        KubernetesDeploymentRepo kubernetesDeploymentRepo) {
        super(edsInstanceService, holderBuilder);
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
    }

    @Override
    public String getKind() {
        return KubernetesResourceKindEnum.DEPLOYMENT.name();
    }

    @Override
    protected Deployment create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        Deployment resource = null;
        try {
            resource = kubernetesDeploymentRepo.find(kubernetes, content);
        } catch (Exception ignored) {
        }
        return resource != null ? resource : kubernetesDeploymentRepo.create(kubernetes, content);
    }

}
