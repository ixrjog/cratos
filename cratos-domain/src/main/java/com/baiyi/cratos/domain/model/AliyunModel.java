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
 * &#064;Date  2025/5/19 10:45
 * &#064;Version 1.0
 */
public class AliyunModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AliyunAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -8707920768026453756L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String username;
        // Aliyun RAM Username
        private String account;
        // 不包含 @domain
        private String ramUsername;
        private String ramLoginUsername;
        private String loginLink;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AliyunPolicy implements Serializable {
        @Serial
        private static final long serialVersionUID = -8707920768026453756L;
        // Aliyun RAM policy
        private EdsAssetVO.Asset asset;
        private String username;
        private String ramUsername;
        private String ramLoginUsername;
        private String loginLink;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResetAliyunAccount extends EdsIdentityVO.CloudAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -8707920768026453756L;
        private Boolean resetPassword;
        private Boolean unbindMFA;
    }

}
