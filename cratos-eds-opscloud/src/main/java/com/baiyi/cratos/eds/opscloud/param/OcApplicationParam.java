package com.baiyi.cratos.eds.opscloud.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 11:10
 * &#064;Version 1.0
 */
public class OcApplicationParam {

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApplicationPageQuery extends HasPageParam {
        @Schema(description = "应用名称")
        private String queryName;
        @Schema(description = "标签ID")
        private Integer tagId;
        @Schema(description = "当标签ID为空才能生效")
        private String tagKey;
        @Schema(description = "展开")
        private Boolean extend;
    }

}
