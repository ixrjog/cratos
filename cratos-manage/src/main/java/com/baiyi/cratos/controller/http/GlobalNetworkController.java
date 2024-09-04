package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.domain.param.network.GlobalNetworkPlanningParam;
import com.baiyi.cratos.domain.param.network.GlobalNetworkSubnetParam;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.facade.GlobalNetworkFacade;
import com.baiyi.cratos.facade.GlobalNetworkPlanningFacade;
import com.baiyi.cratos.facade.GlobalNetworkSubnetFacade;
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

    private final GlobalNetworkPlanningFacade globalNetworkPlanningFacade;

    private final GlobalNetworkSubnetFacade globalNetworkSubnetFacade;

    // ---------- Network

    @Operation(summary = "Pagination query global network")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GlobalNetworkVO.Network>> queryGlobalNetworkPage(
            @RequestBody @Valid GlobalNetworkParam.GlobalNetworkPageQuery pageQuery) {
        return new HttpResult<>(globalNetworkFacade.queryGlobalNetworkPage(pageQuery));
    }

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

    @Operation(summary = "Delete global network by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteGlobalNetworkById(@RequestParam int id) {
        globalNetworkFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    // ---------- Planning

    @Operation(summary = "Pagination query global network planning")
    @PostMapping(value = "/planning/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GlobalNetworkVO.Planning>> queryGlobalNetworkPlanningPage(
            @RequestBody @Valid GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQuery pageQuery) {
        return new HttpResult<>(globalNetworkPlanningFacade.queryGlobalNetworkPlanningPage(pageQuery));
    }

    @Operation(summary = "Add global network planning")
    @PostMapping(value = "/planning/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGlobalNetworkPlanning(
            @RequestBody @Valid GlobalNetworkPlanningParam.AddGlobalNetworkPlanning addGlobalNetworkPlanning) {
        globalNetworkPlanningFacade.addGlobalNetworkPlanning(addGlobalNetworkPlanning);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update global network planning")
    @PutMapping(value = "/planning/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGlobalNetworkPlanning(
            @RequestBody @Valid GlobalNetworkPlanningParam.UpdateGlobalNetworkPlanning updateGlobalNetworkPlanning) {
        globalNetworkPlanningFacade.updateGlobalNetworkPlanning(updateGlobalNetworkPlanning);
        return HttpResult.SUCCESS;
    }

    // ---------- Subnet

    @Operation(summary = "Add global network subnet")
    @PostMapping(value = "/subnet/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addGlobalNetworkSubnet(
            @RequestBody @Valid GlobalNetworkSubnetParam.AddGlobalNetworkSubnet addGlobalNetworkSubnet) {
        globalNetworkSubnetFacade.addGlobalNetworkSubnet(addGlobalNetworkSubnet);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update global network subnet")
    @PutMapping(value = "/subnet/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGlobalNetworkSubnet(
            @RequestBody @Valid GlobalNetworkSubnetParam.UpdateGlobalNetworkSubnet updateGlobalNetworkSubnet) {
        globalNetworkSubnetFacade.updateGlobalNetworkSubnet(updateGlobalNetworkSubnet);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update global network subnet valid")
    @PutMapping(value = "/subnet/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setGlobalNetworkSubnetValidById(@RequestParam int id) {
        globalNetworkSubnetFacade.setGlobalNetworkSubnetValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query global network subnet")
    @PostMapping(value = "/subnet/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<GlobalNetworkVO.Subnet>> queryGlobalNetworkSubnetPage(
            @RequestBody @Valid GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQuery pageQuery) {
        return new HttpResult<>(globalNetworkSubnetFacade.queryGlobalNetworkSubnetPage(pageQuery));
    }

    @Operation(summary = "Delete global network subnet by id")
    @DeleteMapping(value = "/subnet/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteGlobalNetworkSubnetById(@RequestParam int id) {
        globalNetworkSubnetFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
