package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:05
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsCloudflareConfigModel {

    /**
     * https://developers.cloudflare.com/api/
     */
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
    public static class Cred implements ToAuthorization.ToAuthorizationBearer {
        @Schema(description = "Authorization: Bearer <API_TOKEN>")
        private String apiToken;
        @Override
        public String getToken() {
            return apiToken;
        }
    }

}
