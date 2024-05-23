package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationDeployVO;
import com.baiyi.cratos.domain.view.kubernetes.KubernetesDeploymentResponse;
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
 * &#064;Date  2024/5/20 上午10:45
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application")
@Tag(name = "Application")
@RequiredArgsConstructor
public class   ApplicationController {

    @Operation(summary = "按环境查询应用部署详情")
    @PostMapping(value = "/deploy/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesDeploymentResponse<ApplicationDeployVO.Deploy>> queryApplicationDeploy(@RequestBody @Valid ApplicationParam.QueryApplicationDeploy getApplicationDeploy) {
        // TODO
        return null;
    }

}
