package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.facade.BusinessTagFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:51
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/business/tag")
@Tag(name = "Business Tag")
@RequiredArgsConstructor
public class BusinessTagController {

    private final BusinessTagFacade businessTagFacade;

    @Operation(summary = "Query businessTag by business")
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<BusinessTagVO.BusinessTag>> queryBusinessTagByBusiness(@RequestBody @Valid BusinessParam.GetByBusiness getByBusiness) {
        return new HttpResult<>(businessTagFacade.getBusinessTagByBusiness(getByBusiness));
    }

    @Operation(summary = "Query businessTag by tagValue")
    @PostMapping(value = "/query/by/value", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<BusinessTagVO.BusinessTag>> queryBusinessTagByValue(@RequestBody @Valid BusinessTagParam.QueryByValue queryByValue) {
        return new HttpResult<>(businessTagFacade.queryBusinessTagByValue(queryByValue));
    }

    @Operation(summary = "Add businessTag")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addBusinessTag(@RequestBody @Valid BusinessTagParam.AddBusinessTag addBusinessTag) {
        businessTagFacade.addBusinessTag(addBusinessTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update businessTag")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessTag(@RequestBody @Valid BusinessTagParam.UpdateBusinessTag updateBusinessTag) {
        businessTagFacade.updateBusinessTag(updateBusinessTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete businessTag by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteBusinessTagById(@RequestParam @Valid int id) {
        businessTagFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
