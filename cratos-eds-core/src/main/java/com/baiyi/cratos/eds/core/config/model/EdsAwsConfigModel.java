package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.base.HasDnsNameServers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:11
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsAwsConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String id;
        private String name;
        @Schema(description = "可选项公司")
        private String company;
        private String accessKey;
        private String secretKey;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class EC2 {
        private String instances;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class IAM {
        private String accountAlias;
        private String loginUrl;

        public String toLoginUrl() {
            try {
                return StringFormatter.format(loginUrl, accountAlias);
            } catch (Exception e) {
                return loginUrl;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Route53 implements HasDnsNameServers {
        private List<String> nameServers;
    }

}
