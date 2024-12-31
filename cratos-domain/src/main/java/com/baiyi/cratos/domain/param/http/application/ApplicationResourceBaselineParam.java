package com.baiyi.cratos.domain.param.http.application;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 17:08
 * &#064;Version 1.0
 */
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
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaselineMember {
        private String baselineType;
        private Boolean standard;
    }

}
