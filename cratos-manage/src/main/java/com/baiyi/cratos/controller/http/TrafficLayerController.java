package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficIngressTrafficLimitParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerIngressParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/traffic/layer")
@Tag(name = "Traffic Layer")
@RequiredArgsConstructor
public class TrafficLayerController {

    private final TrafficLayerDomainFacade domainFacade;
    private final TrafficLayerRecordFacade recordFacade;
    private final TrafficLayerFacade trafficLayerFacade;
    private final TrafficLayerIngressFacade trafficLayerIngressFacade;
    private final TrafficLayerIngressTrafficLimitFacade trafficLayerIngressTrafficLimitFacade;

    @Operation(summary = "Pagination query traffic layer domain")
    @PostMapping(value = "/domain/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficLayerDomainVO.Domain>> queryTrafficLayerDomainPage(
            @RequestBody @Valid TrafficLayerDomainParam.DomainPageQuery pageQuery) {
        return HttpResult.ofBody(domainFacade.queryDomainPage(pageQuery));
    }

    @Operation(summary = "Add traffic layer domain")
    @PostMapping(value = "/domain/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTrafficLayerDomain(@RequestBody @Valid TrafficLayerDomainParam.AddDomain addDomain) {
        domainFacade.addTrafficLayerDomain(addDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update traffic layer domain")
    @PutMapping(value = "/domain/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTrafficLayerDomain(
            @RequestBody @Valid TrafficLayerDomainParam.UpdateDomain updateDomain) {
        domainFacade.updateTrafficLayerDomain(updateDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete traffic layer domain by id")
    @DeleteMapping(value = "/domain/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTrafficLayerDomainById(@RequestParam int id) {
        domainFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query traffic layer domain env")
    @PostMapping(value = "/domain/env/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<TrafficLayerDomainVO.DomainEnv>> queryTrafficLayerDomainEnv(
            @RequestBody @Valid TrafficLayerDomainParam.QueryDomainEnv queryDomainEnv) {
        return HttpResult.ofBody(domainFacade.queryTrafficLayerDomainEnv(queryDomainEnv));
    }

    @Operation(summary = "Pagination query traffic layer record")
    @PostMapping(value = "/record/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficLayerRecordVO.Record>> queryTrafficLayerRecordPage(
            @RequestBody @Valid TrafficLayerRecordParam.RecordPageQuery pageQuery) {
        return HttpResult.ofBody(recordFacade.queryRecordPage(pageQuery));
    }

    @Operation(summary = "Add traffic layer record")
    @PostMapping(value = "/record/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTrafficLayerRecord(@RequestBody @Valid TrafficLayerRecordParam.AddRecord addRecord) {
        recordFacade.addTrafficLayerRecord(addRecord);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update traffic layer record")
    @PutMapping(value = "/record/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTrafficLayerRecord(
            @RequestBody @Valid TrafficLayerRecordParam.UpdateRecord updateRecord) {
        recordFacade.updateTrafficLayerRecord(updateRecord);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete traffic layer record by id")
    @DeleteMapping(value = "/record/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTrafficLayerRecordById(@RequestParam int id) {
        recordFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query traffic layer record details")
    @PostMapping(value = "/record/details/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TrafficLayerRecordVO.RecordDetails> queryRecordDetails(
            @RequestBody @Valid TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails) {
        return HttpResult.ofBody(trafficLayerFacade.queryRecordDetails(queryRecordDetails));
    }

    @Operation(summary = "Query traffic layer ingress host details")
    @PostMapping(value = "/ingress/host/details/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TrafficLayerIngressVO.IngressDetails> queryIngressHostDetails(
            @RequestBody @Valid TrafficLayerIngressParam.QueryIngressHostDetails queryIngressHostDetails) {
        return HttpResult.ofBody(trafficLayerIngressFacade.queryIngressHostDetails(queryIngressHostDetails));
    }

    @Operation(summary = "Query traffic layer ingress details")
    @PostMapping(value = "/ingress/details/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TrafficLayerIngressVO.IngressDetails> queryIngressDetails(
            @RequestBody @Valid TrafficLayerIngressParam.QueryIngressDetails queryIngressDetails) {
        return HttpResult.ofBody(trafficLayerIngressFacade.queryIngressDetails(queryIngressDetails));
    }

    @Operation(summary = "Query traffic layer ingress traffic-limit")
    @PostMapping(value = "/ingress/traffic-limit/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficLayerIngressVO.IngressTrafficLimit>> queryIngressTrafficLimitPage(
            @RequestBody @Valid TrafficIngressTrafficLimitParam.IngressTrafficLimitPageQuery pageQuery) {
        return HttpResult.ofBody(trafficLayerIngressTrafficLimitFacade.queryIngressTrafficLimitPage(pageQuery));
    }

    @Operation(summary = "Update traffic layer ingress traffic-limit")
    @PutMapping(value = "/ingress/traffic-limit/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateIngressTrafficLimit(
            @RequestBody @Valid TrafficIngressTrafficLimitParam.UpdateIngressTrafficLimit updateIngressTrafficLimit) {
        trafficLayerIngressTrafficLimitFacade.updateIngressTrafficLimit(updateIngressTrafficLimit);
        return HttpResult.SUCCESS;
    }

}
