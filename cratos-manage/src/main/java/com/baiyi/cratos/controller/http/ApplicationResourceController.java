package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesContainerVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDetailsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:21
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application/resource")
@Tag(name = "Application")
@RequiredArgsConstructor
public class ApplicationResourceController {

    private final ApplicationKubernetesDetailsFacade kubernetesDetailsFacade;

    @Operation(summary = "Query application resource kubernetes details")
    @PostMapping(value = "/kubernetes/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<MessageResponse<KubernetesVO.KubernetesDetails>> queryApplicationResourceKubernetesDetails(
            @RequestBody @Valid ApplicationKubernetesParam.QueryKubernetesDetails queryKubernetesDetails) {
        return HttpResult.ofBody(kubernetesDetailsFacade.queryKubernetesDetails(queryKubernetesDetails));
    }

    @Operation(summary = "Query application resource kubernetes deployment options")
    @PostMapping(value = "/kubernetes/deployment/options", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> queryApplicationResourceKubernetesDeploymentOptions(
            @RequestBody @Valid ApplicationKubernetesParam.QueryKubernetesDeploymentOptions queryKubernetesDeploymentOptions) {
        return HttpResult.ofBody(
                kubernetesDetailsFacade.queryKubernetesDeploymentOptions(queryKubernetesDeploymentOptions));
    }

    @Operation(summary = "Delete application resource kubernetes deployment pod")
    @PutMapping(value = "/kubernetes/deployment/pod/del", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteApplicationResourceKubernetesDeploymentPod(
            @RequestBody @Valid ApplicationKubernetesParam.DeleteApplicationResourceKubernetesDeploymentPod deleteApplicationResourceKubernetesDeploymentPod) {
        kubernetesDetailsFacade.deleteApplicationResourceKubernetesDeploymentPod(
                deleteApplicationResourceKubernetesDeploymentPod);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Redeploy application resource kubernetes deployment")
    @PutMapping(value = "/kubernetes/deployment/redeploy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> redeployApplicationResourceKubernetesDeployment(
            @RequestBody @Valid ApplicationKubernetesParam.RedeployApplicationResourceKubernetesDeployment redeployApplicationResourceKubernetesDeployment) {
        kubernetesDetailsFacade.redeployApplicationResourceKubernetesDeployment(
                redeployApplicationResourceKubernetesDeployment);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query kubernetes deployment pod container image version")
    @PostMapping(value = "/kubernetes/deployment/pod/container/image/version/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesContainerVO.ImageVersion> queryApplicationResourceKubernetesDeploymentImageVersion(
            @RequestBody @Valid ApplicationKubernetesParam.QueryKubernetesDeploymentImageVersion queryKubernetesDeploymentImageVersion) {
        return HttpResult.ofBody(
                kubernetesDetailsFacade.queryKubernetesDeploymentImageVersion(queryKubernetesDeploymentImageVersion));
    }

}
