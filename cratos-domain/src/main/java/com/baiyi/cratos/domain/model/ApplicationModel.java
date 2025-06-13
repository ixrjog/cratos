package com.baiyi.cratos.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/13 10:28
 * &#064;Version 1.0
 */
public class ApplicationModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CreateFrontEndApplication implements Serializable {
        @Serial
        private static final long serialVersionUID = -1471573707642296344L;
        private String applicationName;
        // 组件
        private Map<String, String> components;
        private GitLabRepository repository;
        private String domain;
        private String mountPath;
        // ApplicationName
        private String copyFromApplication;
        // LevelTag BusinessTag
        private Map<String, String> tags;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GitLabRepository implements Serializable {
        @Serial
        private static final long serialVersionUID = -882253362611462032L;
        private Integer instanceId;
        private Integer assetId;
        private String sshUrl;
    }

}
