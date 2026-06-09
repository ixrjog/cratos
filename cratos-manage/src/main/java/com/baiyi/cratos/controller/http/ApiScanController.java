package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.generator.ApiScanResult;
import com.baiyi.cratos.facade.security.ApiScanFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security")
@Tag(name = "API Scan")
@RequiredArgsConstructor
public class ApiScanController {

    private final ApiScanFacade apiScanFacade;

    @Operation(summary = "Execute API scan")
    @PostMapping(value = "/api-scan/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> executeScan() {
        apiScanFacade.scan(apiScanFacade.loadScanConfig());
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Execute API scan")
    @PostMapping(value = "/api-scan/application/execute", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> executeScanApplication(@RequestParam String applicationName) {
        apiScanFacade.scan(apiScanFacade.loadScanConfig(), applicationName);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get scan config YAML")
    @GetMapping(value = "/api-scan/config/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<String> getScanConfig() {
        return HttpResult.of(apiScanFacade.getScanConfigYaml());
    }

    @Operation(summary = "Save scan config YAML")
    @PostMapping(value = "/api-scan/config/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveScanConfig(@RequestBody java.util.Map<String, String> param) {
        apiScanFacade.saveScanConfig(param.get("configYaml"));
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query API scan results")
    @PostMapping(value = "/api-scan/results/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<com.baiyi.cratos.domain.DataTable<ApiScanResult>> queryScanResults(
            @RequestBody @jakarta.validation.Valid com.baiyi.cratos.domain.param.http.security.ApiScanParam.ScanResultPageQuery pageQuery) {
        return HttpResult.of(apiScanFacade.queryScanResults(pageQuery));
    }

}
