package com.baiyi.cratos.eds.alimail.model;

import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:26
 * &#064;Version 1.0
 */
public class AlimailToken {

    @Data
    public static class Token implements ToAuthorization.ToAuthorizationBearer, Serializable {
        @Serial
        private static final long serialVersionUID = -4918111534399087602L;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("expires_in")
        private Long expiresIn;

        @Override
        public String getToken() {
            return accessToken;
        }
    }

}
