package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 10:57
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application/resource/baseline")
@Tag(name = "Application Resource Baseline")
@RequiredArgsConstructor
public class ApplicationResourceBaselineController {

    private final ApplicationResourceBaselineFacade applicationResourceBaselineFacade;

    @Operation(summary = "Scan application resource baseline")
    @PostMapping(value = "/scanAll", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> scanApplicationActuator() {
        applicationResourceBaselineFacade.scanAll();
        return HttpResult.SUCCESS;
    }

}
