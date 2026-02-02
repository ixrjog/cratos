package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
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
    public HttpResult<List<BusinessTagVO.BusinessTag>> queryBusinessTagByBusiness(
            @RequestBody @Valid BusinessParam.GetByBusiness getByBusiness) {
        return HttpResult.of(businessTagFacade.getBusinessTagByBusiness(getByBusiness));
    }

    @Operation(summary = "Query businessTag value by tagValue")
    @PostMapping(value = "/query/by/value", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<String>> queryBusinessTagValue(
            @RequestBody @Valid BusinessTagParam.QueryByTag queryByValue) {
        return HttpResult.of(businessTagFacade.queryBusinessTagValue(queryByValue));
    }

    @Operation(summary = "Get countryCode businessTags")
    @GetMapping(value = "/country/code/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<BusinessTag>> getCountryCodeBusinessTags() {
        return HttpResult.of(businessTagFacade.getCountryCodeBusinessTags());
    }

    @Operation(summary = "Save businessTag")
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveBusinessTag(@RequestBody @Valid BusinessTagParam.SaveBusinessTag saveBusinessTag) {
        businessTagFacade.saveBusinessTag(saveBusinessTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Copy businessTag")
    @PostMapping(value = "/copy", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> copyBusinessTag(@RequestBody @Valid BusinessTagParam.CopyBusinessTag copyBusinessTag) {
        businessTagFacade.copyBusinessTag(copyBusinessTag);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add businessTag")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addBusinessTag(@RequestBody @Valid BusinessTagParam.AddBusinessTag addBusinessTag) {
        // TODO
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update businessTag")
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateBusinessTag(
            @RequestBody @Valid BusinessTagParam.UpdateBusinessTag updateBusinessTag) {
        // TODO
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete businessTag by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteBusinessTagById(@RequestParam int id) {
        businessTagFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
