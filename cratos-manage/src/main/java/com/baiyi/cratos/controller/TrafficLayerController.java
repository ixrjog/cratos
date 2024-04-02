package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.facade.TrafficLayerDomainFacade;
import com.baiyi.cratos.facade.TrafficLayerRecordFacade;
import com.baiyi.cratos.facade.TrafficLayerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Pagination query traffic layer domain")
    @PostMapping(value = "/domain/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficLayerDomainVO.Domain>> queryTrafficLayerDomainPage(@RequestBody @Valid TrafficLayerDomainParam.DomainPageQuery pageQuery) {
        return new HttpResult<>(domainFacade.queryDomainPage(pageQuery));
    }

    @Operation(summary = "Add traffic layer domain")
    @PostMapping(value = "/domain/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTrafficLayerDomain(@RequestBody @Valid TrafficLayerDomainParam.AddDomain addDomain) {
        domainFacade.addTrafficLayerDomain(addDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update traffic layer domain")
    @PutMapping(value = "/domain/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTrafficLayerDomain(@RequestBody @Valid TrafficLayerDomainParam.UpdateDomain updateDomain) {
        domainFacade.updateTrafficLayerDomain(updateDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete traffic layer domain by id")
    @DeleteMapping(value = "/domain/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTrafficLayerDomainById(@RequestParam @Valid int id) {
        domainFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add traffic layer record")
    @PostMapping(value = "/record/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addTrafficLayerRecord(@RequestBody @Valid TrafficLayerRecordParam.AddRecord addRecord) {
        recordFacade.addTrafficLayerRecord(addRecord);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update traffic layer record")
    @PutMapping(value = "/record/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateTrafficLayerRecord(@RequestBody @Valid TrafficLayerRecordParam.UpdateRecord updateRecord) {
        recordFacade.updateTrafficLayerRecord(updateRecord);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete traffic layer record by id")
    @DeleteMapping(value = "/record/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTrafficLayerRecordById(@RequestParam @Valid int id) {
        recordFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query traffic layer record details")
    @PostMapping(value = "/record/details/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TrafficLayerRecordVO.RecordDetails> queryRecordDetails(@RequestBody @Valid TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails) {
        return new HttpResult<>(trafficLayerFacade.queryRecordDetails(queryRecordDetails));
    }

}
