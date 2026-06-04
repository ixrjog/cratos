package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.generator.KubernetesDeploymentAppVersionComparison;
import com.baiyi.cratos.domain.param.http.eds.EdsKubernetesNodeParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.domain.view.kubernetes.KubernetesDeploymentAppVersionVO;
import com.baiyi.cratos.facade.kubernetes.KubernetesDeploymentAppVersionComparisonFacade;
import com.baiyi.cratos.facade.kubernetes.KubernetesNodeDetailsFacade;
import com.baiyi.cratos.service.KubernetesDeploymentAppVersionComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 11:36
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/eds/instance/kubernetes")
@Tag(name = "Kubernetes")
@RequiredArgsConstructor
public class ExtDataSourceKubernetesController {

    private final KubernetesNodeDetailsFacade kubernetesNodeDetailsFacade;
    private final KubernetesDeploymentAppVersionComparisonFacade kubernetesDeploymentAppVersionComparisonFacade;
    private final KubernetesDeploymentAppVersionComparisonService comparisonService;

    @Operation(summary = "Query comparison list by country code")
    @GetMapping(value = "/deployment/version/comparison/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<java.util.List<KubernetesDeploymentAppVersionComparison>> queryComparisons(@RequestParam(required = false) String countryCode) {
        java.util.List<KubernetesDeploymentAppVersionComparison> all = comparisonService.selectAll();
        if (countryCode != null && !countryCode.isEmpty()) {
            all = all.stream().filter(c -> countryCode.equals(c.getCountryCode())).toList();
        }
        return HttpResult.of(all);
    }

    @Operation(summary = "Query kubernetes node details")
    @PostMapping(value = "/node/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<MessageResponse<KubernetesNodeVO.KubernetesNodeDetails>> queryKubernetesNodeDetails(
            @RequestBody @Valid EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails queryEdsKubernetesNodeDetails) {
        return HttpResult.of(kubernetesNodeDetailsFacade.queryEdsKubernetesNodeDetails(queryEdsKubernetesNodeDetails));
    }

    @Operation(summary = "Compare deployment version")
    @GetMapping(value = "/deployment/version/compare", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<KubernetesDeploymentAppVersionVO.ComparisonVersion> compareDeploymentVersion(
            @RequestParam int id) {
        return new HttpResult<>(kubernetesDeploymentAppVersionComparisonFacade.compareDeploymentVersion(id));
    }

}
