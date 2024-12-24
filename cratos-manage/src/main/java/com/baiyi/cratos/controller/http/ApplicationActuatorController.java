package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationActuatorParam;
import com.baiyi.cratos.domain.view.application.ApplicationActuatorVO;
import com.baiyi.cratos.facade.application.ApplicationActuatorFacade;
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
 * &#064;Date  2024/12/24 10:20
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application/actuator")
@Tag(name = "Application Actuator")
@RequiredArgsConstructor
public class ApplicationActuatorController {

    private final ApplicationActuatorFacade applicationActuatorFacade;

    @Operation(summary = "Pagination query application actuator")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ApplicationActuatorVO.ApplicationActuator>> queryApplicationActuatorPage(
            @RequestBody @Valid ApplicationActuatorParam.ApplicationActuatorPageQuery pageQuery) {
        return new HttpResult<>(applicationActuatorFacade.queryApplicationActuatorPage(pageQuery));
    }

}
