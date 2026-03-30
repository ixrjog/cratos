package com.baiyi.cratos.domain.param.http.acme;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcmeAccountParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AccountPageQuery extends PageParam {
        private String queryName;
        private Boolean valid;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CreateAccount implements HasSessionUser {

        @NotBlank
        private String name;
        private String email;
        @NotBlank
        private String acmeProvider;
        @Schema(description = "EAB Key ID (External Account Binding)")
        private String eabKid;
        @Schema(description = "EAB HMAC Key")
        private String eabHmacKey;
        @Null
        private String createdBy;

        @Override
        public void setSessionUser(String username) {
            this.createdBy = username;
        }
    }

}
