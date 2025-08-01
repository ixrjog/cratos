package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.eds.cratos.CratosAssetParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.facade.EdsFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Query eds instance type options")
    @GetMapping(value = "/instance/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getEdsInstanceTypeOptions() {
        return HttpResult.ofBody(EdsInstanceTypeEnum.toOptions());
    }

    @Operation(summary = "Pagination query eds instance")
    @PostMapping(value = "/instance/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsInstanceVO.EdsInstance>> queryEdsInstancePage(
            @RequestBody @Valid EdsInstanceParam.InstancePageQuery pageQuery) {
        return HttpResult.ofBody(edsFacade.queryEdsInstancePage(pageQuery));
    }

    @Operation(summary = "Get eds instance")
    @GetMapping(value = "/instance/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsInstanceVO.EdsInstance> getEdsInstanceById(@RequestParam int instanceId) {
        return HttpResult.ofBody(edsFacade.getEdsInstanceById(instanceId));
    }

    @Operation(summary = "Register eds instance")
    @PostMapping(value = "/instance/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> registerEdsInstance(
            @RequestBody @Valid EdsInstanceParam.RegisterInstance registerEdsInstance) {
        edsFacade.registerEdsInstance(registerEdsInstance);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Unregister eds instance")
    @DeleteMapping(value = "/instance/unregister", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> unregisterEdsInstance(@RequestParam int id) {
        edsFacade.unregisterEdsInstance(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update eds instance")
    @PutMapping(value = "/instance/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateEdsInstance(
            @RequestBody @Valid EdsInstanceParam.UpdateInstance updateEdsInstance) {
        edsFacade.updateEdsInstance(updateEdsInstance);
        return HttpResult.SUCCESS;
    }

    // Config

    @Operation(summary = "Pagination query eds config")
    @PostMapping(value = "/config/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsConfigVO.EdsConfig>> queryEdsConfigPage(
            @RequestBody @Valid EdsConfigParam.EdsConfigPageQuery pageQuery) {
        return HttpResult.ofBody(edsFacade.queryEdsConfigPage(pageQuery));
    }

    @Operation(summary = "Query eds config by id")
    @GetMapping(value = "/config/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsConfigVO.EdsConfig> getEdsConfigById(@RequestParam int configId) {
        return HttpResult.ofBody(edsFacade.getEdsConfigById(configId));
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

    @Operation(summary = "Update eds config valid")
    @PutMapping(value = "/config/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setEdsConfigValidById(@RequestParam int id) {
        edsFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete eds config by id")
    @DeleteMapping(value = "/config/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEdsConfigById(@RequestParam int id) {
        edsFacade.deleteEdsConfigById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Import eds instance asset")
    @PostMapping(value = "/instance/asset/import", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> importEdsInstanceAsset(
            @RequestBody @Valid EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        edsFacade.importEdsInstanceAsset(importInstanceAsset);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query eds instance asset")
    @PostMapping(value = "/instance/asset/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<EdsAssetVO.Asset>> queryEdsInstanceAssetPage(
            @RequestBody @Valid EdsInstanceParam.AssetPageQuery assetPageQuery) {
        return HttpResult.ofBody(edsFacade.queryEdsInstanceAssetPage(assetPageQuery));
    }

    @Operation(summary = "Get eds instance asset")
    @GetMapping(value = "/instance/asset/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsAssetVO.Asset> getEdsInstanceAsset(@RequestParam int id) {
        return HttpResult.ofBody(edsFacade.getEdsInstanceAsset(id));
    }

    @Operation(summary = "Delete eds instance asset")
    @DeleteMapping(value = "/instance/asset/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEdsInstanceAsset(
            @RequestBody @Valid EdsInstanceParam.DeleteInstanceAsset deleteInstanceAsset) {
        edsFacade.deleteEdsInstanceAsset(deleteInstanceAsset);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query eds instance asset support manual")
    @GetMapping(value = "/instance/asset/support/manual/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsAssetVO.SupportManual<?>> getEdsInstanceAssetSupportManual(@RequestParam String instanceType,
                                                                                    String assetType) {
        return HttpResult.ofBody(edsFacade.getEdsInstanceAssetSupportManual(instanceType, assetType));
    }

    @Operation(summary = "Delete eds asset by id")
    @DeleteMapping(value = "/asset/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteEdsAssetById(@RequestParam int id) {
        edsFacade.deleteEdsAssetById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query eds asset index by id")
    @GetMapping(value = "/asset/index/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<EdsAssetVO.Index>> queryAssetIndexByAssetId(@RequestParam int assetId) {
        return HttpResult.ofBody(edsFacade.queryAssetIndexByAssetId(assetId));
    }

    @Operation(summary = "Query eds asset by uniqueKey")
    @PostMapping(value = "/asset/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsAssetVO.Asset> queryAssetByUniqueKey(
            @RequestBody @Valid EdsInstanceParam.QueryAssetByUniqueKey queryAssetByUniqueKey) {
        return HttpResult.ofBody(edsFacade.queryAssetByUniqueKey(queryAssetByUniqueKey));
    }

    @Operation(summary = "Add eds cratos asset")
    @PostMapping(value = "/instance/cratos/asset/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addInstanceCratosAsset(
            @RequestBody @Valid CratosAssetParam.AddCratosAsset addCratosAsset) {
        edsFacade.addInstanceCratosAsset(addCratosAsset);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update eds cratos asset")
    @PutMapping(value = "/instance/cratos/asset/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateInstanceCratosAsset(
            @RequestBody @Valid CratosAssetParam.UpdateCratosAsset updateCratosAsset) {
        edsFacade.updateInstanceCratosAsset(updateCratosAsset);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get to business target")
    @GetMapping(value = "/asset/to/business/target/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<EdsAssetVO.AssetToBusiness<?>> getToBusinessTarget(@RequestParam int assetId) {
        return HttpResult.ofBody(edsFacade.getToBusinessTarget(assetId));
    }

}
