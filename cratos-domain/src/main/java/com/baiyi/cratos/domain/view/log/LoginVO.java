package com.baiyi.cratos.domain.view.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/1/10 11:37
 * @Version 1.0
 */
public class LoginVO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class Login {

        private String username;
        @Schema(description = "DisplayName")
        private String name;
        private String uuid;
        private String token;

    }

}
