package com.baiyi.cratos.domain.param.server;

import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/4/15 上午11:07
 * @Version 1.0
 */
public class ServerAccountParam {

    @Data
    @Schema
    public static class AddServerAccount implements IToTarget<ServerAccount> {

        private Integer id;

        @NotBlank
        private String name;

        @NotBlank
        private String username;

        private Integer credentialId;

        private Boolean sudo;

        @NotBlank
        private String protocol;

        @NotNull
        private Boolean valid;

        private String comment;
    }

    @Data
    @Schema
    public static class UpdateServerAccount implements IToTarget<ServerAccount> {

        private Integer id;

        @NotBlank
        private String name;

        @NotBlank
        private String username;

        private Integer credentialId;

        private Boolean sudo;

        @NotBlank
        private String protocol;

        @NotNull
        private Boolean valid;

        private String comment;
    }

}
