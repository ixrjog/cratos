package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.risk.RiskEventImpactParam;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.risk.RiskEventGraphVO;
import com.baiyi.cratos.domain.view.risk.RiskEventVO;
import com.baiyi.cratos.facade.RiskEventFacade;
import com.baiyi.cratos.facade.RiskEventGraphFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:02
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/risk/event")
@Tag(name = "Risk Event")
@RequiredArgsConstructor
public class RiskEventController {

    private final RiskEventFacade riskEventFacade;

    private final RiskEventGraphFacade riskEventGraphFacade;

    @Operation(summary = "Pagination query risk event")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RiskEventVO.Event>> queryRiskEventPage(@RequestBody @Valid RiskEventParam.RiskEventPageQuery pageQuery) {
        return new HttpResult<>(riskEventFacade.queryRiskEventPage(pageQuery));
    }

    @Operation(summary = "Get risk event")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<RiskEventVO.Event> getRiskEventById(@RequestParam @Valid int id) {
        return new HttpResult<>(riskEventFacade.getRiskEventById(id));
    }

    @Operation(summary = "Add risk event")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRiskEvent(@RequestBody @Valid RiskEventParam.AddRiskEvent addRiskEvent) {
        riskEventFacade.addRiskEvent(addRiskEvent);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update risk event")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRiskEvent(@RequestBody @Valid RiskEventParam.UpdateRiskEvent updateRiskEvent) {
        riskEventFacade.updateRiskEvent(updateRiskEvent);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add risk event impact")
    @PostMapping(value = "/impact/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRiskEventImpact(@RequestBody @Valid RiskEventImpactParam.AddRiskEventImpact addRiskEventImpact) {
        riskEventFacade.addRiskEventImpact(addRiskEventImpact);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update risk event impact")
    @PutMapping(value = "/impact/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRiskEventImpact(@RequestBody @Valid RiskEventImpactParam.UpdateRiskEventImpact updateRiskEventImpact) {
        riskEventFacade.updateRiskEventImpact(updateRiskEventImpact);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete risk event impact by id")
    @DeleteMapping(value = "/impact/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRiskEventImpactById(@RequestParam @Valid int id) {
        riskEventFacade.deleteRiskEventImpactById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get SLA report year options")
    @GetMapping(value = "/report/year/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getYearOptions() {
        return new HttpResult<>(riskEventGraphFacade.getYearOptions());
    }

    @Operation(summary = "Query SLA graph")
    @PostMapping(value = "/graph/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<RiskEventGraphVO.Graph> queryGraph(@RequestBody @Valid RiskEventParam.RiskEventGraphQuery riskEventGraphQuery) {
        return new HttpResult<>(riskEventGraphFacade.queryGraph(riskEventGraphQuery));
    }

}
