package com.baiyi.cratos.domain.param.http.acme;

import com.baiyi.cratos.domain.HasSessionUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcmeAccountParam {

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
        @Null
        private String createdBy;

        @Override
        public void setSessionUser(String username) {
            this.createdBy = username;
        }
    }

}
