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
 * &#064;Date  2024/6/5 上午11:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsGodaddyConfigModel {

    @Data
    @NoArgsConstructor
    @Schema(description = "godaddy.com")
    public static class Godaddy implements HasDnsNameServers, IEdsConfigModel {
        @Schema(description = "凭据")
        private Cred cred;
        private String customerId;
        private List<String> nameServers;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred implements ToAuthorization.ToAuthorizationSsoKey {
        @Schema(description = "Authorization: sso-key <Key:Secret>")
        private String key;
        @Schema(description = "Authorization: sso-key <Key:Secret>")
        private String secret;
    }

}
