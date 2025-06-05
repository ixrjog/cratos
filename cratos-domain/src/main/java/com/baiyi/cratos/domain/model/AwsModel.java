package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/5 14:37
 * &#064;Version 1.0
 */
public class AwsModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AwsPolicy implements Serializable {
        @Serial
        private static final long serialVersionUID = -8810202638325492590L;
        // Aws IAM policy
        private EdsAssetVO.Asset asset;
        private EdsIdentityVO.CloudAccount cloudAccount;
        private String username;
        private String iamUsername;
        private String iamLoginUsername;
        private String loginLink;
    }

}
