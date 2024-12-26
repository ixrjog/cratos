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
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDetailsFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.wrapper.application.ApplicationWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.domain.view.base.OptionsVO.NO_OPTIONS_AVAILABLE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationKubernetesDetailsFacadeImpl implements ApplicationKubernetesDetailsFacade {

    private final ApplicationResourceService applicationResourceService;
    private final ApplicationKubernetesDeploymentConverter deploymentConverter;
    private final ApplicationKubernetesServiceConverter serviceConverter;
    private final ApplicationService applicationService;
    private final ApplicationWrapper applicationWrapper;

    @Override
    public MessageResponse<KubernetesVO.KubernetesDetails> queryKubernetesDetails(
            ApplicationKubernetesParam.QueryKubernetesDetails queryKubernetesDetails) {
        return MessageResponse.<KubernetesVO.KubernetesDetails>builder()
                .body(buildKubernetesDetails(queryKubernetesDetails))
                .topic(HasTopic.APPLICATION_KUBERNETES_DETAILS)
                .build();
    }

    @Override
    public OptionsVO.Options queryKubernetesDeploymentOptions(
            ApplicationKubernetesParam.QueryKubernetesDeploymentOptions queryKubernetesDeploymentOptions) {
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                queryKubernetesDeploymentOptions.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(),
                queryKubernetesDeploymentOptions.getNamespace());
        if (CollectionUtils.isEmpty(resources)) {
            return NO_OPTIONS_AVAILABLE;
        }
        return OptionsVO.Options.builder()
                .options(resources.stream()
                        .map(e -> OptionsVO.Option.builder()
                                .value(e.getName())
                                .label(e.getDisplayName())
                                .build())
                        .sorted()
                        .toList())
                .build();
    }

    private KubernetesVO.Workloads makeWorkloads(ApplicationKubernetesParam.QueryKubernetesDetails param) {
        List<KubernetesDeploymentVO.Deployment> deployments = deploymentConverter.toResourceVO(
                applicationResourceService.queryApplicationResource(param.getApplicationName(),
                        EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace()));
        return KubernetesVO.Workloads.builder()
                .deployments(deployments)
                .build();
    }

    private KubernetesVO.Network makeNetwork(ApplicationKubernetesParam.QueryKubernetesDetails param) {
        List<KubernetesServiceVO.Service> services = serviceConverter.toResourceVO(
                applicationResourceService.queryApplicationResource(param.getApplicationName(),
                        EdsAssetTypeEnum.KUBERNETES_SERVICE.name(), param.getNamespace()));
        return KubernetesVO.Network.builder()
                .services(services)
                .build();
    }

    private KubernetesVO.KubernetesDetails buildKubernetesDetails(
            ApplicationKubernetesParam.QueryKubernetesDetails param) {
        Application application = applicationService.getByName(param.getApplicationName());
        if (application == null) {
            return KubernetesVO.KubernetesDetails.failed("The application does not exist.");
        }
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                param.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace(),
                param.getName());
        return CollectionUtils.isEmpty(resources) ? KubernetesVO.KubernetesDetails.failed(
                "The kubernetes resource bound to the application does not exist.") : KubernetesVO.KubernetesDetails.builder()
                .application(applicationWrapper.convert(application))
                .namespace(param.getNamespace())
                .workloads(makeWorkloads(param))
                .network(makeNetwork(param))
                .build();
    }

}
