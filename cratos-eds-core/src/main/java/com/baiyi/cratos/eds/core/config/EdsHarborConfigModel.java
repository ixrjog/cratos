package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/15 下午5:33
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsHarborConfigModel {

    private static final String API_V2 = "/api/v2.0";

    @Data
    @NoArgsConstructor
    @Schema
    public static class Harbor implements IEdsConfigModel {
        private String version;
        private String url;
        @Schema(description = "凭据")
        private Cred cred;
        private EdsInstance edsInstance;

        public String acqUrl() {
            if (StringUtils.hasText(this.url)) {
                return url + API_V2;
            } else {
                return null;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred implements ToAuthorization.ToAuthorizationBasic {
        @Schema(description = "authorization: Basic <USERNAME:TOKEN -> Base64>")
        private String username;
        private String token;

        @Override
        public String getPassword() {
            return this.token;
        }
    }

}
