package com.baiyi.cratos.domain.param.traffic;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:42
 * @Version 1.0
 */
public class TrafficLayerDomainParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class DomainPageQuery extends PageParam {

        @Schema(description = "Query by name")
        private String queryName;

    }

}
