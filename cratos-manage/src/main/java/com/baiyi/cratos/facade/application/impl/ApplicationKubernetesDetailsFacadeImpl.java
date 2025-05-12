package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.exception.KubernetesResourceOperationException;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.converter.impl.ApplicationKubernetesDeploymentConverter;
import com.baiyi.cratos.converter.impl.ApplicationKubernetesServiceConverter;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesServiceVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.eds.core.config.EdsOpscloudConfigModel;
import com.baiyi.cratos.eds.core.config.loader.EdsOpscloudConfigLoader;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.opscloud.model.OcLeoVO;
import com.baiyi.cratos.eds.opscloud.param.OcLeoParam;
import com.baiyi.cratos.eds.opscloud.repo.OcLeoRepo;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDetailsFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.workorder.holder.ApplicationDeletePodTokenHolder;
import com.baiyi.cratos.workorder.holder.token.ApplicationDeletePodToken;
import com.baiyi.cratos.wrapper.application.ApplicationWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.SHORT_TERM;
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
    private final AccessControlFacade accessControlFacade;
    private final EdsInstanceService edsInstanceService;
    private final EdsFacade edsFacade;
    private final EdsOpscloudConfigLoader edsOpscloudConfigLoader;
    private final ApplicationDeletePodTokenHolder applicationDeletePodTokenHolder;
    private final EdsAssetService edsAssetService;

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
                        EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace(), param.getName()));
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
                param.getApplicationName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), param.getNamespace());
        if (CollectionUtils.isEmpty(resources)) {
            KubernetesVO.KubernetesDetails.failed("The kubernetes resource bound to the application does not exist.");
        }
        KubernetesVO.KubernetesDetails kubernetesDetails = KubernetesVO.KubernetesDetails.builder()
                .application(applicationWrapper.convert(application))
                .namespace(param.getNamespace())
                .workloads(makeWorkloads(param))
                .network(makeNetwork(param))
                .build();
        return (KubernetesVO.KubernetesDetails) accessControlFacade.invoke(kubernetesDetails);
    }

    @Override
    @Cacheable(cacheNames = SHORT_TERM, key = "'OC:LEO:BUILD:IMAGE:VERSION:'+ #queryKubernetesDeploymentImageVersion.image", unless = "#result == null")
    public KubernetesContainerVO.ImageVersion queryKubernetesDeploymentImageVersion(
            ApplicationKubernetesParam.QueryKubernetesDeploymentImageVersion queryKubernetesDeploymentImageVersion) {
        return edsFacade.queryValidEdsInstanceByType(EdsInstanceTypeEnum.OPSCLOUD.name())
                .stream()
                .findFirst()
                .map(instance -> {
                    EdsOpscloudConfigModel.Opscloud opscloud = edsOpscloudConfigLoader.getConfig(
                            instance.getConfigId());
                    OcLeoParam.QueryBuildImageVersion queryParam = OcLeoParam.QueryBuildImageVersion.builder()
                            .image(queryKubernetesDeploymentImageVersion.getImage())
                            .build();
                    HttpResult<OcLeoVO.BuildImage> httpResult = OcLeoRepo.queryBuildImageVersion(opscloud, queryParam);
                    if (httpResult.isSuccess()) {
                        OcLeoVO.BuildImage buildImage = httpResult.getBody();
                        return KubernetesContainerVO.ImageVersion.builder()
                                .image(buildImage.getImage())
                                .versionName(buildImage.getVersionName())
                                .versionDesc(buildImage.getVersionDesc())
                                .build();
                    }
                    return KubernetesContainerVO.ImageVersion.notFound(
                            queryKubernetesDeploymentImageVersion.getImage());
                })
                .orElse(KubernetesContainerVO.ImageVersion.notFound(queryKubernetesDeploymentImageVersion.getImage()));
    }

    @Override
    public void deleteApplicationResourceKubernetesDeploymentPod(
            ApplicationKubernetesParam.DeleteApplicationResourceKubernetesDeploymentPod deleteApplicationResourceKubernetesDeploymentPod) {
        ApplicationDeletePodToken.Token deleteToken = applicationDeletePodTokenHolder.getToken(
                SessionUtils.getUsername(), deleteApplicationResourceKubernetesDeploymentPod.getApplicationName());
        if (!deleteToken.getValid()) {
            KubernetesResourceOperationException.runtime("Unauthorized access");
        }
        List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                deleteApplicationResourceKubernetesDeploymentPod.getApplicationName(),
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(),
                deleteApplicationResourceKubernetesDeploymentPod.getNamespace(),
                deleteApplicationResourceKubernetesDeploymentPod.getDeploymentName());
        if (CollectionUtils.isEmpty(resources)) {
            KubernetesResourceOperationException.runtime("The deployment={} resource does not exist.",
                    deleteApplicationResourceKubernetesDeploymentPod.getDeploymentName());
        }
        resources.forEach(resource -> {
            EdsAsset deploymentAsset = edsAssetService.getById(resource.getBusinessId());


        });


    }

}
