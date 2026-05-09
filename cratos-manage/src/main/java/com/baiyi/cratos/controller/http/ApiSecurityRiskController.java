package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskReportVO;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskVO;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.facade.ApiSecurityRiskFacade;
import com.baiyi.cratos.facade.ApiSecurityTestFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:07
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/security")
@Tag(name = "API Security Risk")
@RequiredArgsConstructor
public class ApiSecurityRiskController {

    private final ApiSecurityRiskFacade apiSecurityRiskFacade;
    private final ApiSecurityTestFacade apiSecurityTestFacade;

    @Operation(summary = "Add api security risk")
    @PostMapping(value = "/risk/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRisk(@RequestBody @Valid ApiSecurityRiskParam.AddRisk addRisk) {
        apiSecurityRiskFacade.addRisk(addRisk);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update api security risk")
    @PutMapping(value = "/risk/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRisk(@RequestBody @Valid ApiSecurityRiskParam.UpdateRisk updateRisk) {
        apiSecurityRiskFacade.updateRisk(updateRisk);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query api security risk")
    @PostMapping(value = "/risk/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApiSecurityRiskVO.Risk>> queryRiskPage(
            @RequestBody @Valid ApiSecurityRiskParam.RiskPageQuery pageQuery) {
        return HttpResult.of(apiSecurityRiskFacade.queryRiskPage(pageQuery));
    }

    @Operation(summary = "Get api security risk report")
    @GetMapping(value = "/risk/report/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ApiSecurityRiskReportVO.Report> getReport() {
        return HttpResult.of(apiSecurityRiskFacade.getReport());
    }

    @Operation(summary = "Delete api security risk by id")
    @DeleteMapping(value = "/risk/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRiskById(@RequestParam int id) {
        apiSecurityRiskFacade.deleteRiskById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Call test api")
    @PostMapping(value = "/risk/test/api/call", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<GenericCall.Response> callTestApi(@RequestBody @Valid ApiTestParam.CallApi callApi) {
        return HttpResult.of(apiSecurityTestFacade.callTestApi(callApi));
    }

    @Operation(summary = "Get auto sign map yaml")
    @GetMapping(value = "/risk/test/auto/sign/map/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> getAutoSignMapYaml() {
        return HttpResult.of(apiSecurityTestFacade.getAutoSignMapYaml());
    }

    @Operation(summary = "Save auto sign map")
    @PostMapping(value = "/risk/test/auto/sign/map/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveAutoSignMap(@RequestBody @Valid ApiTestParam.SaveSignMap saveSignMap) {
        apiSecurityTestFacade.saveAutoSignMap(saveSignMap);
        return HttpResult.SUCCESS;
    }

}
