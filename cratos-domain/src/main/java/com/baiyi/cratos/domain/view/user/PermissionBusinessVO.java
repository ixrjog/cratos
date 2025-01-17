package com.baiyi.cratos.domain.view.user;

import com.baiyi.cratos.domain.BaseBusiness;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 11:16
 * &#064;Version 1.0
 */
public class PermissionBusinessVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class PermissionBusiness implements BaseBusiness.HasBusiness, Serializable {
        @Serial
        private static final long serialVersionUID = -4844155821760523819L;
        private String name;
        private String displayName;
        private String businessType;
        private Integer businessId;
        private String comment;
    }

}
