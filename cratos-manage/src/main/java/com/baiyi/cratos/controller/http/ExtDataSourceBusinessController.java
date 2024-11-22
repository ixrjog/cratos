package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.eds.EdsBusinessParam;
import com.baiyi.cratos.domain.view.eds.EdsBusinessVO;
import com.baiyi.cratos.facade.EdsBusinessFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午3:30
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/eds/business")
@Tag(name = "External Datasource Business")
@RequiredArgsConstructor
public class ExtDataSourceBusinessController {

    private final EdsBusinessFacade edsBusinessFacade;

    /**
     * 使用appName查询Kubernetes实例中所有的关联资源, Service、Deployment、Ingress
     * @param kubernetesInstanceResourceQuery
     * @return
     */
    @Operation(summary = "Query eds kubernetes instance resource")
    @GetMapping(value = "/kubernetes/resource/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsBusinessVO.KubernetesInstanceResource> queryKubernetesInstanceResource(
            @RequestBody @Valid EdsBusinessParam.KubernetesInstanceResourceQuery kubernetesInstanceResourceQuery) {
        return new HttpResult<>(edsBusinessFacade.queryKubernetesInstanceResource(kubernetesInstanceResourceQuery));
    }

}
