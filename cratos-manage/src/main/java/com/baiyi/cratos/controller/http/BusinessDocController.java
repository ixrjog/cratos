package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.doc.BusinessDocParam;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.BusinessDocFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 09:48
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/business/doc")
@Tag(name = "Business Doc")
@RequiredArgsConstructor
public class BusinessDocController {

    private final BusinessDocFacade businessDocFacade;

    @Operation(summary = "Query businessDoc by business")
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<BusinessDocVO.BusinessDoc>> queryBusinessDocByBusiness(@RequestBody @Valid BusinessParam.GetByBusiness getByBusiness) {
        return HttpResult.of(businessDocFacade.getBusinessDocByBusiness(getByBusiness));
    }

    @Operation(summary = "Add businessDoc")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addBusinessDoc(@RequestBody @Valid BusinessDocParam.AddBusinessDoc addBusinessDoc) {
        businessDocFacade.addBusinessDoc(addBusinessDoc);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update businessDoc")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessDoc(@RequestBody @Valid BusinessDocParam.UpdateBusinessDoc updateBusinessDoc) {
        businessDocFacade.updateBusinessDoc(updateBusinessDoc);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete businessDoc by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteBusinessDocById(@RequestParam int id) {
        businessDocFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
