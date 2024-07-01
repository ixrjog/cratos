package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasRegionModel;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:52
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsAliyunConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aliyun implements HasRegionModel, IEdsConfigModel {

        private String version;
        // default
        private String regionId;

        private Set<String> regionIds;

        @Schema(description = "凭据")
        private Cred cred;

        private ALB alb;

        private OSS oss;

        private Domain domain;

        private EdsInstance edsInstance;

        private ARMS arms;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {

        private String uid;

        private String name;

        private String company;

        private String accessKeyId;

        private String accessKeySecret;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ALB {

        private List<String> endpoints;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class OSS {

        private List<String> endpoints;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Domain {

        private String endpoint;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ARMS {

        private String regionId;

        private String endpoint;

        private String home;

    }

}
