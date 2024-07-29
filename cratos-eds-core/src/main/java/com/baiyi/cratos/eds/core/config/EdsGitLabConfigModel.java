package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/21 11:24
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsGitLabConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class GitLab implements IEdsConfigModel {
        private Api api;
        private SystemHooks systemHooks;
        private GitFlow gitFlow;
        @Schema(description = "凭据")
        private Cred cred;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String token;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Api {
        private String version;
        private Integer connectTimeout;
        private Integer readTimeout;
        private String url;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class SystemHooks {
        @Schema(description = "回调token")
        private String token;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class GitFlow {
        private Boolean enabled;
        @Schema(description = "环境分支限制")
        private Map<String, List<String>> filter;
    }

}
