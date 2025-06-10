package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasRegionsModel;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.google.common.base.Joiner;
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

    public static final String DMS_ENDPOINT = "dms-enterprise.aliyuncs.com";

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aliyun implements HasRegionsModel, IEdsConfigModel {
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
        private MongoDB mongoDB;
        private ONS ons;
        private ACR acr;
        private RAM ram;
        private KMS kms;
        private DMS dms;
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
    public static class KMS {
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

    @Data
    @NoArgsConstructor
    @Schema
    public static class MongoDB {
        private List<String> endpoints;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ACR {
        private List<String> regionIds;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ONS {
        private RocketMQ v5;
        private RocketMQ v4;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RocketMQ {
        private List<String> endpoints;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class DMS {
        private String endpoint;
        private Long tid;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RAM {
        private String domain;
        private String loginUrl;

        public String toLoginUrl() {
            try {
                return StringFormatter.format(loginUrl, domain);
            } catch (Exception e) {
                return loginUrl;
            }
        }

        public String toUsername(String username) {
            return Joiner.on("@")
                    .join(username, domain);
        }

    }

}
