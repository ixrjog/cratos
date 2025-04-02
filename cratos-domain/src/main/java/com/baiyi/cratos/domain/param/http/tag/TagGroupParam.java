package com.baiyi.cratos.domain.param.http.tag;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/2 14:52
 * &#064;Version 1.0
 */
public class TagGroupParam {

    @Data
    @Schema
    public static class GetGroupOptions {
        private String queryName;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ComputerGroupPageQuery extends PageParam {
        @NotBlank
        private String tagGroup;
        private String queryName;
    }

}
