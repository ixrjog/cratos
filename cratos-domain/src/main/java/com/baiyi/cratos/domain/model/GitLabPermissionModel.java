package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 17:39
 * &#064;Version 1.0
 */
public class GitLabPermissionModel {

    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Permission implements Serializable {
        @Serial
        private static final long serialVersionUID = -4989331115974120790L;
        private EdsAssetVO.Asset target;
        private String role;
    }

}
