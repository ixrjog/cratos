package com.baiyi.cratos.eds.alimail.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:26
 * &#064;Version 1.0
 */
public class AlimailTokenResult {

    @Data
    public static class Token {
        private String id;
        private String type;
        private List<String> hosts;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("expires_in")
        private Long expiresIn;
    }

}
