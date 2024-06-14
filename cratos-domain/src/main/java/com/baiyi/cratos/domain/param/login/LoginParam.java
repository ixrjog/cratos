package com.baiyi.cratos.domain.param.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/10 11:38
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class LoginParam {

    @Builder
    @Data
    @NoArgsConstructor
    @Schema
    @AllArgsConstructor
    public static class Login {

        @NotBlank(message = "用户名不能为空")
        private String username;

        private String password;

        @Schema(description = "One Time Password")
        private String otp;

        public boolean isEmptyPassword() {
            return StringUtils.isEmpty(password);
        }
    }

}
