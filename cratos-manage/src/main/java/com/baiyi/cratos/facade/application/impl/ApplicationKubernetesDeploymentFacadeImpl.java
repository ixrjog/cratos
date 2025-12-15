package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.common.exception.KubernetesException;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDeploymentFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/23 14:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationKubernetesDeploymentFacadeImpl implements ApplicationKubernetesDeploymentFacade {

    private final ApplicationService applicationService;
    private final ApplicationResourceService applicationResourceService;
    private final EdsAssetService edsAssetService;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    @Override
    public Deployment queryApplicationResourceKubernetesDeployment(
            ApplicationKubernetesParam.QueryKubernetesDeployment queryKubernetesDeployment) {
        Application application = applicationService.getByName(queryKubernetesDeployment.getApplicationName());
        if (application == null) {
            KubernetesException.runtime("应用不存在: {}", queryKubernetesDeployment.getApplicationName());
        }
        ApplicationResource uk = ApplicationResource.builder()
                .applicationName(application.getName())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(queryKubernetesDeployment.getAssetId())
                .build();
        ApplicationResource applicationResource = applicationResourceService.getByUniqueKey(uk);
        if (applicationResource == null) {
            KubernetesException.runtime("应用资源不存在: {} - {}", queryKubernetesDeployment.getApplicationName(),
                    queryKubernetesDeployment.getAssetId());
        }
        if (!EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name()
                .equals(applicationResource.getResourceType())) {
            KubernetesException.runtime("应用资源类型错误, 期望: {} - 实际: {}",
                    EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), applicationResource.getResourceType());
        }
        EdsAsset edsAsset = edsAssetService.getById(queryKubernetesDeployment.getAssetId());
        if (edsAsset == null) {
            KubernetesException.runtime("资产不存在: assetId={}", queryKubernetesDeployment.getAssetId());
        }
        return getDeployment(applicationResource.getNamespace(), edsAsset);
    }

    @SuppressWarnings("unchecked")
    private Deployment getDeployment(String namespace, EdsAsset edsAsset) {
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, ?>) holderBuilder.newHolder(
                edsAsset.getInstanceId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        return kubernetesDeploymentRepo.get(holder.getInstance()
                .getConfig(), namespace, edsAsset.getName());
    }

}
