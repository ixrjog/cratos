package com.baiyi.cratos.domain.param.http.user;

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
 * &#064;Date  2025/7/1 14:54
 * &#064;Version 1.0
 */
public class UserFavoriteParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddUserFavorite implements BaseBusiness.HasBusiness , Serializable {
        @Serial
        private static final long serialVersionUID = 7079888545195226283L;
        private String businessType;
        private Integer businessId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RemoveUserFavorite implements BaseBusiness.HasBusiness, Serializable  {
        @Serial
        private static final long serialVersionUID = 4746129912006363469L;
        private String businessType;
        private Integer businessId;
    }

}
