package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.facade.BusinessTagFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Get businessTag by business")
    @PostMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<BusinessTagVO.BusinessTag>> getBusinessTagByBusiness(@RequestBody @Valid BusinessTagParam.GetByBusiness getByBusiness) {
        return new HttpResult<>(businessTagFacade.getBusinessTagByBusiness(getByBusiness));
    }

}
