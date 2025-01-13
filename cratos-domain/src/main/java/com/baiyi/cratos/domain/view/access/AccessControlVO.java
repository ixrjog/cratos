package com.baiyi.cratos.domain.view.access;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/13 16:01
 * &#064;Version 1.0
 */
public class AccessControlVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AccessControl implements Serializable {
        @Serial
        private static final long serialVersionUID = -117053221590362418L;
        private Boolean permission;
        private String businessType;

        public static AccessControl unauthorized(String businessType) {
            return AccessControl.builder()
                    .permission(false)
                    .businessType(businessType)
                    .build();
        }

        public static AccessControl authorized(String businessType) {
            return AccessControl.builder()
                    .permission(true)
                    .businessType(businessType)
                    .build();
        }
    }

}
