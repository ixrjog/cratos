package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.converter.impl.ApplicationKubernetesDeploymentConverter;
import com.baiyi.cratos.converter.impl.ApplicationKubernetesServiceConverter;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesServiceVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.application.ApplicationKubernetesWorkloadFacade;
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
public class ApplicationKubernetesDeploymentFacadeImpl implements ApplicationKubernetesWorkloadFacade {

    private final ApplicationResourceService applicationResourceService;
    private final ApplicationKubernetesDeploymentConverter deploymentConverter;
    private final ApplicationKubernetesServiceConverter serviceConverter;
    private final ApplicationService applicationService;
    private final ApplicationWrapper applicationWrapper;

    @Override
    public MessageResponse<KubernetesVO.KubernetesWorkload> queryKubernetesWorkload(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload queryApplicationKubernetesDeployment) {
        return MessageResponse.<KubernetesVO.KubernetesWorkload>builder()
                .body(buildKubernetesDetails(queryApplicationKubernetesDeployment))
                .topic(HasTopic.APPLICATION_KUBERNETES_WORKLOAD)
                .build();
    }

    private List<KubernetesDeploymentVO.Deployment> buildDeploymentResources(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload param) {
        return deploymentConverter.toResourceVO(
                applicationResourceService.queryApplicationResource(param.getApplicationName(),
                        EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace()));
    }

    private List<KubernetesServiceVO.Service> buildServiceResources(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload param) {
        return serviceConverter.toResourceVO(
                applicationResourceService.queryApplicationResource(param.getApplicationName(),
                        EdsAssetTypeEnum.KUBERNETES_SERVICE.name(), param.getNamespace()));
    }

    private KubernetesVO.KubernetesWorkload buildKubernetesDetails(
            ApplicationKubernetesParam.QueryApplicationResourceKubernetesWorkload param) {
        Application application = applicationService.getByName(param.getApplicationName());
        if (application == null) {
            return KubernetesVO.KubernetesWorkload.failed("The application does not exist.");
        }
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                param.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace());
        return CollectionUtils.isEmpty(resources) ? KubernetesVO.KubernetesWorkload.failed(
                "The kubernetes resource bound to the application does not exist.") : KubernetesVO.KubernetesWorkload.builder()
                .application(applicationWrapper.convert(application))
                .namespace(param.getNamespace())
                .deployments(buildDeploymentResources(param))
                .services(buildServiceResources(param))
                .build();
    }

}
