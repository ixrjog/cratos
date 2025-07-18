package com.baiyi.cratos.domain.param.http.env;


import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;


/**
 * @Author baiyi
 * @Date 2024/3/19 13:56
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EnvParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class EnvPageQuery extends PageParam {
        @Schema(description = "Query by name")
        private String queryName;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class QueryEnvByGroupValue {
        private String groupValue;
    }

    @Data
    @Schema
    public static class AddEnv implements IToTarget<Env> {
        @NotBlank
        private String envName;
        @NotBlank
        private String color;
        @NotBlank
        private String promptColor;
        @NotNull
        private Integer lifecycle;
        @NotNull
        private Integer seq;
        @NotNull
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateEnv implements IToTarget<Env> {
        private Integer id;
        private String envName;
        private String color;
        private String promptColor;
        @NotNull
        private Integer lifecycle;
        private Integer seq;
        private Boolean valid;
        private String comment;
    }

}
