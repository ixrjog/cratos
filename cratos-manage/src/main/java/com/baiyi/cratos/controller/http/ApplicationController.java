package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.facade.ApplicationFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:45
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application")
@Tag(name = "Application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationFacade applicationFacade;
    private final ApplicationResourceFacade applicationResourceFacade;

    @Operation(summary = "Pagination query application")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryApplicationPage(
            @RequestBody @Valid ApplicationParam.ApplicationPageQuery pageQuery) {
        return HttpResult.ofBody(applicationFacade.queryApplicationPage(pageQuery));
    }

    @Operation(summary = "Get application by name")
    @PostMapping(value = "/get/by/name", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ApplicationVO.Application> getApplicationByName(
            @RequestBody @Valid ApplicationParam.GetApplication getApplication) {
        return HttpResult.ofBody(applicationFacade.getApplicationByName(getApplication));
    }

    @Operation(summary = "Add application")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplication(@RequestBody @Valid ApplicationParam.AddApplication addApplication) {
        applicationFacade.addApplication(addApplication);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Scan application")
    @PostMapping(value = "/scan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanApplicationResource(@RequestBody @Valid ApplicationParam.ScanResource scanResource) {
        applicationFacade.scanApplicationResource(scanResource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Scan all application")
    @PostMapping(value = "/all/scan", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanAllApplicationResource() {
        applicationFacade.scanAllApplicationResource();
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update application")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateApplication(
            @RequestBody @Valid ApplicationParam.UpdateApplication updateApplication) {
        applicationFacade.updateApplication(updateApplication);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update application valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setApplicationValidById(@RequestParam int id) {
        applicationFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete application by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteApplicationById(@RequestParam int id) {
        applicationFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete application resource by id")
    @DeleteMapping(value = "/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteApplicationResourceById(@RequestParam int id) {
        applicationResourceFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get application resource namespace options")
    @GetMapping(value = "/resource/namespace/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getResourceNamespaceOptions() {
        return HttpResult.ofBody(applicationResourceFacade.getNamespaceOptions());
    }

    @Operation(summary = "Get my application resource namespace options")
    @GetMapping(value = "/resource/namespace/my/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getMyResourceNamespaceOptions(
            @RequestParam @Valid @NotBlank String applicationName) {
        ApplicationParam.GetMyApplicationResourceNamespaceOptions param = ApplicationParam.GetMyApplicationResourceNamespaceOptions.builder()
                .applicationName(applicationName)
                .build();
        return HttpResult.ofBody(applicationResourceFacade.getMyApplicationResourceNamespaceOptions(param));
    }

}
