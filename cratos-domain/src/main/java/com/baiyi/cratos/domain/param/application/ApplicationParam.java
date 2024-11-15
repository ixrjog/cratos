package com.baiyi.cratos.domain.param.application;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ApplicationParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ApplicationPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public ApplicationPageQueryParam toParam() {
            return ApplicationPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .build();
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ApplicationPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @Schema
    public static class AddApplication implements IToTarget<Application> {
        @Null
        private Integer id;
        private String name;
        private Boolean valid;
        private String config;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateApplication implements IToTarget<Application> {
        @NotNull
        private Integer id;
        private String name;
        private Boolean valid;
        private String config;
        private String comment;
    }

}
