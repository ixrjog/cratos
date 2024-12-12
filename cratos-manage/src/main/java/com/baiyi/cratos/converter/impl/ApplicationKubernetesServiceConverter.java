package com.baiyi.cratos.converter.impl;

import com.baiyi.cratos.converter.base.BaseKubernetesResourceConverter;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesServiceVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesServiceRepo;
import com.baiyi.cratos.facade.application.builder.KubernetesServiceBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.EnvService;
import com.google.api.client.util.Maps;
import io.fabric8.kubernetes.api.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/3 11:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ApplicationKubernetesServiceConverter extends BaseKubernetesResourceConverter<KubernetesServiceVO.Service, Service> {

    private final KubernetesServiceRepo kubernetesServiceRepo;

    public ApplicationKubernetesServiceConverter(EdsInstanceService edsInstanceService,
                                                 EdsInstanceProviderHolderBuilder holderBuilder,
                                                 EdsAssetService edsAssetService,
                                                 KubernetesServiceRepo kubernetesServiceRepo, EnvService envService) {
        super(edsInstanceService, holderBuilder, edsAssetService, envService);
        this.kubernetesServiceRepo = kubernetesServiceRepo;
    }

    @Override
    public List<KubernetesServiceVO.Service> toResourceVO(List<ApplicationResource> resources) {
        Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap = Maps.newHashMap();
        return resources.stream()
                .map(e -> to(edsInstanceConfigMap, e))
                .filter(Objects::nonNull)
                .toList();
    }

    private KubernetesServiceVO.Service to(Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap,
                                           ApplicationResource resource) {
        int assetId = resource.getBusinessId();
        EdsAsset edsAsset = edsAssetService.getById(assetId);
        if (Objects.isNull(edsAsset)) {
            return null;
        }
        EdsInstance edsInstance = edsInstanceService.getById(edsAsset.getInstanceId());
        EdsKubernetesConfigModel.Kubernetes kubernetes = getEdsConfig(edsInstanceConfigMap, edsInstance);
        final String namespace = resource.getNamespace();
        Service service = kubernetesServiceRepo.get(kubernetes, namespace, resource.getName());
        return KubernetesServiceBuilder.newBuilder()
                .withService(service)
                .build();
    }

    @Override
    protected EdsAssetTypeEnum getEdsAssetType() {
        return EdsAssetTypeEnum.KUBERNETES_SERVICE;
    }

}
