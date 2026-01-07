package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.enums.TrafficRecordTargetTypes;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.facade.TrafficRouteFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/12 15:20
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/traffic/route")
@Tag(name = "Traffic Route")
@RequiredArgsConstructor
public class TrafficRouteController {

    private final TrafficRouteFacade trafficRouteFacade;

    @Operation(summary = "Query dns resolver eds instances")
    @GetMapping(value = "/dns/resolver/instance/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<EdsInstanceVO.EdsInstance>> queryDnsResolverInstances() {
        return HttpResult.of(trafficRouteFacade.queryDnsResolverInstances());
    }

    @Operation(summary = "Pagination query traffic route")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficRouteVO.Route>> queryTrafficRoutePage(
            @RequestBody @Valid TrafficRouteParam.RoutePageQuery pageQuery) {
        return HttpResult.of(trafficRouteFacade.queryRoutePage(pageQuery));
    }

    @Operation(summary = "Add traffic route")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTrafficRoute(@RequestBody @Valid TrafficRouteParam.AddRoute addRoute) {
        trafficRouteFacade.addTrafficRoute(addRoute);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete traffic route by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTrafficRouteById(@RequestParam int id) {
        trafficRouteFacade.deleteTrafficRouteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get traffic route by id")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TrafficRouteVO.Route> getTrafficRouteById(@RequestParam int id) {
        return HttpResult.of(trafficRouteFacade.getTrafficRouteById(id));
    }

    @Operation(summary = "Get traffic record target type options")
    @GetMapping(value = "/record/target/type/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getTrafficRecordTargetTypeOptions() {
        return HttpResult.of(TrafficRecordTargetTypes.toOptions());
    }

    @Operation(summary = "Add traffic record target")
    @PostMapping(value = "/record/target/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTrafficRecordTarget(
            @RequestBody @Valid TrafficRouteParam.AddRecordTarget addRecordTarget) {
        trafficRouteFacade.addTrafficRecordTarget(addRecordTarget);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update traffic record target")
    @PutMapping(value = "/record/target/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTrafficRecordTarget(
            @RequestBody @Valid TrafficRouteParam.UpdateRecordTarget updateRecordTarget) {
        trafficRouteFacade.updateTrafficRecordTarget(updateRecordTarget);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Switch to traffic record target")
    @PutMapping(value = "/record/target/switch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> switchToTarget(
            @RequestBody @Valid TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        trafficRouteFacade.switchToTarget(switchRecordTarget);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete traffic record target by id")
    @DeleteMapping(value = "/record/target/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTrafficRecordTargetById(@RequestParam int id) {
        trafficRouteFacade.deleteTrafficRecordTargetById(id);
        return HttpResult.SUCCESS;
    }

}
