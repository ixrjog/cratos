package com.baiyi.cratos.domain.view.log;

import com.baiyi.cratos.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/10 11:37
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expiredTime;
        private Long maxAge;
    }

}
