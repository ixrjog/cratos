package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.facade.inspection.InspectionFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/12 下午2:07
 * &#064;Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/inspection")
@Tag(name = "Inspection")
@RequiredArgsConstructor
public class InspectionController {

    @Operation(summary = "Send inspection notification")
    @GetMapping(value = "/notification/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> sendInspectionNotification() {
        InspectionFactory.doTask();
        return HttpResult.SUCCESS;
    }

}
