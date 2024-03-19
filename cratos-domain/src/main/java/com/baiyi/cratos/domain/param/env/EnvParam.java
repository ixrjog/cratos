package com.baiyi.cratos.domain.param.env;


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


/**
 * @Author baiyi
 * @Date 2024/3/19 13:56
 * @Version 1.0
 */
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
    @Schema
    public static class AddEnv implements IToTarget<Env> {

        @NotBlank
        private String envName;

        @NotBlank
        private String color;

        @NotBlank
        private Integer promptColor;

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

        private Integer promptColor;

        private Integer seq;

        private Boolean valid;

        private String comment;

    }

}
