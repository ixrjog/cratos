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
 * &#064;Date  2026/3/24 10:32
 * &#064;Version 1.0
 */
public class GcpModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class IamMember implements Serializable {
        @Serial
        private static final long serialVersionUID = -5614206689381260223L;
        private EdsInstanceVO.EdsInstance edsInstance;
        // Google Account
        private String member;
        // google Project
        private String projectName;
        private String projectId;
        // Cratos Username
        private String username;
        private String type;
        private String loginLink = "https://console.cloud.google.com/";
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MemberRole implements Serializable {
        @Serial
        private static final long serialVersionUID = 4030841647562227713L;
        // GCP IAM Role
        private EdsAssetVO.Asset asset;
        // Google Account
        private String member;
        // google Project
        private String projectName;
        private String projectId;
        private IamRole role;

        private String username;
        private String type;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class IamRole implements Serializable {
        @Serial
        private static final long serialVersionUID = 294720560055500735L;
        private String name;
        private String title;
        private String description;
    }

}
