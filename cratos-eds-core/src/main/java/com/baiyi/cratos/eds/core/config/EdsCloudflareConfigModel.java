package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:05
 * @Version 1.0
 */
public class EdsCloudflareConfigModel {

    // https://developers.cloudflare.com/api/

    public static final String CLIENT_API = "https://api.cloudflare.com/client/v4";

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cloudflare implements IEdsConfigModel {

        @Schema(description = "凭据")
        private Cred cred;

        private EdsInstance edsInstance;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {

//        @Schema(description = "X-Auth-Email: 123")
//        private String apiEmail;
//
//        @Schema(description = "X-Auth-Key: 123")
//        private String apiKey;
//
//        @Schema(description = "X-Auth-User-Service-Key: 123")
//        private String userServiceKey;

        @Schema(description = "Authorization: Bearer <API_TOKEN>")
        private String apiToken;

    }

}
