package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.application.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.facade.application.ApplicationKubernetesDetailsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return HttpResult.of(kubernetesDetailsFacade.queryKubernetesDetails(queryKubernetesDetails));
    }

    @Operation(summary = "Query application resource kubernetes deployment options")
    @PostMapping(value = "/kubernetes/deployment/options", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> queryApplicationResourceKubernetesDeploymentOptions(
            @RequestBody @Valid ApplicationKubernetesParam.QueryKubernetesDeploymentOptions queryKubernetesDeploymentOptions) {
        return new HttpResult<>(
                kubernetesDetailsFacade.queryKubernetesDeploymentOptions(queryKubernetesDeploymentOptions));
    }

}
