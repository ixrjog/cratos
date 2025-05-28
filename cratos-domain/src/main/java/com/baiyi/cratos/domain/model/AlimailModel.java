package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.generator.EdsAsset;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/28 09:51
 * &#064;Version 1.0
 */
public class AlimailModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResetAlimailAccount implements Serializable {

        @Serial
        private static final long serialVersionUID = -495089687163509346L;

        /**
         * Alimail User
         */
        @Null
        private EdsAsset asset;
        private String username;

        @NotNull
        private Integer assetId;

        private String userId;
        private String mail;
        private String loginUrl;

    }

}
