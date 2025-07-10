package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.finops.FinOpsParam;
import com.baiyi.cratos.domain.view.finops.FinOpsVO;
import com.baiyi.cratos.facade.fin.FinOpsFacade;
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
 * &#064;Date  2025/6/17 13:29
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/finops")
@Tag(name = "FinOps")
@RequiredArgsConstructor
public class FinOpsController {

    private final FinOpsFacade finOpsFacade;

    @Operation(summary = "Query application cost")
    @PostMapping(value = "/app/cost/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<FinOpsVO.AppCost> queryAppCost(@RequestBody @Valid FinOpsParam.QueryAppCost queryAppCost) {
        return HttpResult.ofBaseException(finOpsFacade.queryAppCost(queryAppCost));
    }

}
