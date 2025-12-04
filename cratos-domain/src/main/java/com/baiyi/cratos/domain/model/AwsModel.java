package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    public static class AwsAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -5073917251800369081L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String username;
        private String accountId;
        private String iamUsername;
        private String loginUsername;
        private String loginLink;
    }

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
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResetAwsAccount extends EdsIdentityVO.CloudAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -3469496619079586291L;
        private Boolean resetPassword;
        private Boolean resetMFA;
    }

}
