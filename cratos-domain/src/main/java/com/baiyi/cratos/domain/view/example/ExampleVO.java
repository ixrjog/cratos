package com.baiyi.cratos.domain.view.example;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2023/9/14 14:13
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ExampleVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class HelloWorld {

        public static final HelloWorld EXAMPLE = HelloWorld.builder().build();

        @Builder.Default
        private String msg = "Hello World!";

        @Builder.Default
        private Date requestTime = new Date();

    }

}