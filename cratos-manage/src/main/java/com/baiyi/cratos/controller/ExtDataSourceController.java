package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.facade.EdsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/eds")
@Tag(name = "External Datasource")
@RequiredArgsConstructor
public class ExtDataSourceController {

    private final EdsFacade edsFacade;

    // Instance

    @Operation(summary = "Pagination query eds instance")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsInstanceVO.EdsInstance>> queryEdsInstancePage(@RequestBody @Valid EdsInstanceParam.EdsInstancePageQuery pageQuery) {
        return new HttpResult<>(edsFacade.queryEdsInstancePage(pageQuery));
    }

    @Operation(summary = "Register eds instance")
    @PostMapping(value = "/instance/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> registerEdsInstance(@RequestBody @Valid EdsInstanceParam.RegisterEdsInstance registerEdsInstance) {
        edsFacade.registerEdsInstance(registerEdsInstance);
        return HttpResult.SUCCESS;
    }

    // Config

    @Operation(summary = "Pagination query eds config")
    @PostMapping(value = "/config/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsConfigVO.EdsConfig>> queryEdsConfigPage(@RequestBody @Valid EdsConfigParam.EdsConfigPageQuery pageQuery) {
        return new HttpResult<>(edsFacade.queryEdsConfigPage(pageQuery));
    }

    @Operation(summary = "Query eds config by id")
    @GetMapping(value = "/config/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsConfigVO.EdsConfig> getEdsConfigById(@RequestParam int configId) {
        return new HttpResult<>(edsFacade.getEdsConfigById(configId));
    }

    @Operation(summary = "Add eds config")
    @PostMapping(value = "/config/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addEdsConfig(@RequestBody @Valid EdsConfigParam.AddEdsConfig addEdsConfig) {
        edsFacade.addEdsConfig(addEdsConfig);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update eds config")
    @PutMapping(value = "/config/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateEdsConfig(@RequestBody @Valid EdsConfigParam.UpdateEdsConfig updateEdsConfig) {
        edsFacade.updateEdsConfig(updateEdsConfig);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete eds config by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEdsConfigById(@RequestParam @Valid int id) {
        edsFacade.deleteEdsConfigById(id);
        return HttpResult.SUCCESS;
    }

}
