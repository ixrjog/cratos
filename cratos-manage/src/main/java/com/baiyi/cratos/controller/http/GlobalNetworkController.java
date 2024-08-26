package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:43
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/global/network")
@Tag(name = "Global Network")
@RequiredArgsConstructor
public class GlobalNetworkController {

    private final GlobalNetworkFacade globalNetworkFacade;

    @Operation(summary = "Add global network")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGlobalNetwork(
            @RequestBody @Valid GlobalNetworkParam.AddGlobalNetwork addGlobalNetwork) {
        globalNetworkFacade.addGlobalNetwork(addGlobalNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update global network")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGlobalNetwork(
            @RequestBody @Valid GlobalNetworkParam.UpdateGlobalNetwork updateGlobalNetwork) {
        globalNetworkFacade.updateGlobalNetwork(updateGlobalNetwork);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update global network valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setGlobalNetworkValidById(@RequestParam int id) {
        globalNetworkFacade.setGlobalNetworkValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query global network")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GlobalNetworkVO.GlobalNetwork>> queryGlobalNetworkPage(
            @RequestBody @Valid GlobalNetworkParam.GlobalNetworkPageQuery pageQuery) {
        return new HttpResult<>(globalNetworkFacade.queryGlobalNetworkPage(pageQuery));
    }

    @Operation(summary = "Delete global network by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteGlobalNetworkById(@RequestParam int id) {
        globalNetworkFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
