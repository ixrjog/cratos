package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.util.StringFormatter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:04
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsHwcConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String uid;
        private String username;
        private String accessKey;
        private String secretKey;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Project {
        private String name;
        private String id;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class IAM {
        private String loginUrl;
        public String toLoginUrl(String mainUsername) {
            try {
                return StringFormatter.format(loginUrl, mainUsername);
            } catch (Exception e) {
                return loginUrl;
            }
        }
    }

}
