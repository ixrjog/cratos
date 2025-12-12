package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.facade.TrafficRouteFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Pagination query traffic route")
    @PostMapping(value = "/route/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficRouteVO.Route>> queryTrafficRoutePage(
            @RequestBody @Valid TrafficRouteParam.RoutePageQuery pageQuery) {
        return HttpResult.of(trafficRouteFacade.queryRoutePage(pageQuery));
    }

}
