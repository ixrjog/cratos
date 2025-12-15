package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.base.HasDnsNameServers;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    public static class NLB {
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
        private String appOverview;
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
    public static class DNS implements HasDnsNameServers {
        private List<String> nameServers;
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
