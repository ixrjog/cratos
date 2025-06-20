package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/20 10:48
 * &#064;Version 1.0
 */
public class AliyunKmsModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CreateSecret implements Serializable {
        @Serial
        private static final long serialVersionUID = -5846538132637578197L;
        private EdsInstanceVO.EdsInstance edsInstance;
        // KMS instance
        private EdsAssetVO.Asset kmsInstance;
        private String endpoint;
        private String secretName;
        private String secretData;
        private String versionId;
        private String encryptionKeyId;
        private String description;
    }

}
