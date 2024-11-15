package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.facade.ApplicationFacade;
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
 * &#064;Date  2024/5/20 上午10:45
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application")
@Tag(name = "Application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationFacade applicationFacade;

    @Operation(summary = "Pagination query application")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationVO.Application>> queryApplicationPage(@RequestBody @Valid ApplicationParam.ApplicationPageQuery pageQuery) {
        return new HttpResult<>(applicationFacade.queryApplicationPage(pageQuery));
    }

}
