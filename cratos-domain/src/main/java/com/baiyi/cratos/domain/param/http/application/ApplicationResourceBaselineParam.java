package com.baiyi.cratos.domain.param.http.application;

import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.commit.CommitParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 17:08
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ApplicationResourceBaselineParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ApplicationResourceBaselinePageQuery extends PageParam {
        private String applicationName;
        private String namespace;
        private String framework;
        private Boolean standard;
        private BaselineMember byMemberType;
        private Boolean isQueryCanary;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaselineMember {
        @NotBlank(message = "The parameter baselineType cannot be empty")
        private String baselineType;
        @NotNull(message = "The parameter standard must be specified")
        private Boolean standard;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MergeToBaselineCommit {
        private Integer baselineId;
        private CommitParam.Commit commit;
    }

}
