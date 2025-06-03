package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/3 09:59
 * &#064;Version 1.0
 */
public class AwsTransferModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SFTPUser implements Serializable {
        @Serial
        private static final long serialVersionUID = -2964848765938844239L;
        // AWS Transfer SFTP Server
        private EdsAssetVO.Asset asset;
        private String username;
        private String transferServerEndpoint;
        private String publicKey;
        private String keyFingerprint;
        private String description;
    }

}
