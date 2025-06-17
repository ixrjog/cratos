package com.baiyi.cratos.domain.param.http.finops;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/17 09:32
 * &#064;Version 1.0
 */
public class FinOpsParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryAppCost implements Serializable {
        @Serial
        private static final long serialVersionUID = 8961374797623014807L;
        private List<AllocationCategory> allocationCategories;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AllocationCategory implements Serializable {
        @Serial
        private static final long serialVersionUID = 8961374797623014807L;
        private String name;
        @Schema(description = "货币代码, e.g., USD CNY")
        private String currencyCode;
        private Long amount;
    }

}
