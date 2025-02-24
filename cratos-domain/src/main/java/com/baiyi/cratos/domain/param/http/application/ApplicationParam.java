package com.baiyi.cratos.domain.param.http.application;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
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
        private Boolean valid;
        private List<Integer> idList;

        public ApplicationPageQueryParam toParam() {
            return ApplicationPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .valid(valid)
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
        private Boolean valid;
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

    @Data
    @Schema
    public static class ScanResource {
        @NotBlank
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GetMyApplicationResourceNamespaceOptions implements HasSessionUser {
        private String applicationName;
        private String sessionUser;
    }

}
