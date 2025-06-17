package com.baiyi.cratos.domain.view.finops;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/17 13:08
 * &#064;Version 1.0
 */
public class FinOpsVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AppCost implements Serializable {
        @Serial
        private static final long serialVersionUID = 5704464294665420093L;
        private String costTable;
        private String costDetailsTable;
    }

}
