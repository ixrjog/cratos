package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainRecordVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.facade.TrafficLayerDomainFacade;
import com.baiyi.cratos.facade.TrafficLayerFacade;
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
 * @Author baiyi
 * @Date 2024/3/29 11:22
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/traffic/layer")
@Tag(name = "Traffic Layer")
@RequiredArgsConstructor
public class TrafficLayerController {

    private final TrafficLayerDomainFacade trafficLayerDomainFacade;

    private final TrafficLayerFacade trafficLayerFacade;

    @Operation(summary = "Pagination query traffic layer domain")
    @PostMapping(value = "/domain/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<TrafficLayerDomainVO.Domain>> queryTrafficLayerDomainPage(@RequestBody @Valid TrafficLayerDomainParam.DomainPageQuery pageQuery) {
        return new HttpResult<>(trafficLayerDomainFacade.queryDomainPage(pageQuery));
    }

    @Operation(summary = "Query traffic layer domain record details")
    @PostMapping(value = "/domain/record/details/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<TrafficLayerDomainRecordVO.RecordDetails> queryRecordDetails(@RequestBody @Valid TrafficLayerDomainRecordParam.QueryRecordDetails queryRecordDetails) {
        return new HttpResult<>(trafficLayerFacade.queryRecordDetails(queryRecordDetails));
    }

}
