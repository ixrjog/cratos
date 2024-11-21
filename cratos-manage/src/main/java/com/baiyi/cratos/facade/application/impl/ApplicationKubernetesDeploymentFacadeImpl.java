package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.common.MessageResponse;
import com.baiyi.cratos.converter.ApplicationKubernetesDeploymentConverter;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.param.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDeploymentFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.wrapper.application.ApplicationWrapper;
import com.google.api.client.util.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

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
    private final ApplicationKubernetesDeploymentConverter deploymentConverter;
    private final ApplicationService applicationService;
    private final ApplicationWrapper applicationWrapper;

    @Override
    public MessageResponse<KubernetesVO.KubernetesDetails> queryKubernetesDeploymentDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails queryApplicationKubernetesDeployment) {
        return MessageResponse.<KubernetesVO.KubernetesDetails>builder()
                .body(toKubernetesDetails(queryApplicationKubernetesDeployment))
                .topic(MessageResponse.Topics.APPLICATION_KUBERNETES_DETAILS)
                .build();
    }

    public KubernetesVO.KubernetesDetails toKubernetesDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails param) {
        Application application = applicationService.getByName(param.getApplicationName());
        if (application == null) {
            return KubernetesVO.KubernetesDetails.failed("The application does not exist.");
        }
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                param.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace());
        if (CollectionUtils.isEmpty(resources)) {
            return KubernetesVO.KubernetesDetails.failed(
                    "The kubernetes resource bound to the application does not exist.");
        }
        Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap = Maps.newHashMap();
        List<KubernetesDeploymentVO.Deployment> deployments = resources.stream()
                .map(e -> deploymentConverter.to(edsInstanceConfigMap, e))
                .toList();
        return KubernetesVO.KubernetesDetails.builder()
                .application(applicationWrapper.convert(application))
                .namespace(param.getNamespace())
                .deployments(deployments)
                .build();
    }

}
