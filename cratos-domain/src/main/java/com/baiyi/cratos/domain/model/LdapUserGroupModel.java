package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.generator.EdsAsset;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/26 15:04
 * &#064;Version 1.0
 */
public class LdapUserGroupModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Role implements Serializable {
        @Serial
        private static final long serialVersionUID = 5215226390679158598L;
        private EdsAsset asset;
        private String group;
        private String description;
    }

}
