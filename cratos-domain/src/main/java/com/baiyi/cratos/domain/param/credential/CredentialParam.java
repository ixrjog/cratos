package com.baiyi.cratos.domain.param.credential;

import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:29
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CredentialParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CredentialPageQuery extends PageParam {
        @Schema(description = "Query by name")
        private String queryName;
        @Schema(description = "Query by credentialType")
        private String credentialType;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddCredential implements IToTarget<Credential> {
        private Integer id;
        private String title;
        @NotBlank(message = "CredentialType must be specified.")
        private String credentialType;
        /**
         * 用户名
         */
        private String username;
        /**
         * 指纹
         */
        private String fingerprint;
        /**
         * 凭据内容
         */
        private String credential;
        private String credential2;
        /**
         * 密码短语
         */
        private String passphrase;
        private Boolean privateCredential;
        /**
         * 有效
         */
        private Boolean valid;
        private String comment;
        @NotNull(message = "ExpiredTime must be specified.")
        private Date expiredTime;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateCredential implements IToTarget<Credential> {
        @NotNull(message = "The ID must be specified.")
        private Integer id;
        private String title;
        private String credentialType;
        /**
         * 用户名
         */
        private String username;
        /**
         * 指纹
         */
        private String fingerprint;
        /**
         * 凭据内容
         */
        private String credential;
        private String credential2;
        /**
         * 密码短语
         */
        private String passphrase;
        private Boolean privateCredential;
        /**
         * 有效
         */
        private Boolean valid;
        private String comment;
    }

}
