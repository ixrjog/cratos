package com.baiyi.cratos.domain.param.credential;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:29
 * @Version 1.0
 */
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

}
