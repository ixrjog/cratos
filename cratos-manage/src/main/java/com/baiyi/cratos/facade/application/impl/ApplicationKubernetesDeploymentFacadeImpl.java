package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.converter.ApplicationKubernetesDeploymentConverter;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDeploymentFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.wrapper.application.ApplicationWrapper;
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
    private final ApplicationKubernetesDeploymentConverter deploymentConverter;
    private final ApplicationService applicationService;
    private final ApplicationWrapper applicationWrapper;

    @Override
    public MessageResponse<KubernetesVO.KubernetesDetails> queryKubernetesDeploymentDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails queryApplicationKubernetesDeployment) {
        return MessageResponse.<KubernetesVO.KubernetesDetails>builder()
                .body(buildKubernetesDetails(queryApplicationKubernetesDeployment))
                .topic(HasTopic.APPLICATION_KUBERNETES_DETAILS)
                .build();
    }

    private KubernetesVO.KubernetesDetails buildKubernetesDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesDetails param) {
        Application application = applicationService.getByName(param.getApplicationName());
        if (application == null) {
            return KubernetesVO.KubernetesDetails.failed("The application does not exist.");
        }
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                param.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace());
        return CollectionUtils.isEmpty(resources) ? KubernetesVO.KubernetesDetails.failed(
                "The kubernetes resource bound to the application does not exist.") : KubernetesVO.KubernetesDetails.builder()
                .application(applicationWrapper.convert(application))
                .namespace(param.getNamespace())
                .deployments(deploymentConverter.toDeployments(resources))
                .build();
    }

}
