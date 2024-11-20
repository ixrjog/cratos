package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.common.MessageResponse;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.ApplicationResourceKubernetesVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDeploymentFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationKubernetesDeploymentFacadeImpl implements ApplicationKubernetesDeploymentFacade {

    private final ApplicationResourceService applicationResourceService;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceService edsInstanceService;

    @Override
    public MessageResponse<ApplicationResourceKubernetesVO.KubernetesDetails> queryKubernetesDeploymentDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails queryApplicationKubernetesDeployment) {
        return null;
    }

    public void todo(ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails param) {
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                param.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace());
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }

        resources.stream()
                .map(e -> {
                    int assetId = e.getBusinessId();
                    EdsAsset edsAsset = edsAssetService.getById(assetId);
                    EdsInstance edsInstance = edsInstanceService.getById(edsAsset.getInstanceId());

                    return e;

                })
                .toList();


    }

}
