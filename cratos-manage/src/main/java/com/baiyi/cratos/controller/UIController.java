package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.view.user.UIVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2024/1/23 13:43
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/ui")
@Tag(name = "UI")
@RequiredArgsConstructor
public class UIController {

    @Operation(summary = "Get UI")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UIVO.UI> getUI() {
        // TODO
        return null;
    }

}
