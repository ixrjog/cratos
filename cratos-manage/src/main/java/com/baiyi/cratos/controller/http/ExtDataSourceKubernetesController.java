package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.param.http.eds.EdsKubernetesNodeParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesNodeDetailsFacade;
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
 * &#064;Date  2024/12/19 11:36
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/eds/instance/kubernetes")
@Tag(name = "Application")
@RequiredArgsConstructor
public class ExtDataSourceKubernetesController {

    private final KubernetesNodeDetailsFacade kubernetesNodeDetailsFacade;

    @Operation(summary = "Query kubernetes node details")
    @PostMapping(value = "/node/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<MessageResponse<KubernetesNodeVO.KubernetesNodeDetails>> queryKubernetesNodeDetails(
            @RequestBody @Valid EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails queryEdsKubernetesNodeDetails) {
        return HttpResult.ofBaseException(
                kubernetesNodeDetailsFacade.queryEdsKubernetesNodeDetails(queryEdsKubernetesNodeDetails));
    }

}
