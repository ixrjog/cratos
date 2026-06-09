package com.baiyi.cratos.domain.param.http.security;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class ApiScanParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ScanResultPageQuery extends PageParam {
        @Schema(description = "应用名称")
        private String queryName;
        @Schema(description = "路径")
        private String queryPath;
    }

}
