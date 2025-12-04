package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasDnsNameServers;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午3:15
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsGandiConfigModel {

    @Data
    @NoArgsConstructor
    @Schema(description = "gandi.net")
    public static class Gandi implements HasDnsNameServers, IEdsConfigModel {
        @Schema(description = "凭据")
        private Cred cred;
        private List<String> nameServers;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred implements ToAuthorization.ToAuthorizationBearer, ToAuthorization.ToAuthorizationApikey {
        @Schema(description = "Authorization: Bearer <AccessToken>")
        private String accessToken;
        @Schema(description = "Authorization: Apikey <Apikey>")
        private String apikey;

        @Override
        public String getToken() {
            return accessToken;
        }
    }

}
