package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.view.example.ExampleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author baiyi
 * @Date 2024/1/2 16:47
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/example")
@Tag(name = "接口例子")
@RequiredArgsConstructor
public class ExampleController {

    @Operation(summary = "Example for Hello World")
    @GetMapping(value = "/helloWorld", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<ExampleVO.HelloWorld> helloWorld() {
       return new HttpResult<>(ExampleVO.HelloWorld.EXAMPLE);
    }

}