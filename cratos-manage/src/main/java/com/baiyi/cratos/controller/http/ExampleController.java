package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.view.example.ExampleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2024/1/2 16:47
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/example")
@Tag(name = "Example")
@RequiredArgsConstructor
public class ExampleController {

    @Operation(summary = "Hello World")
    @GetMapping(value = "/helloWorld", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ExampleVO.HelloWorld> helloWorld() {
        return HttpResult.of(ExampleVO.HelloWorld.EXAMPLE);
    }

}