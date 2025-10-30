package com.baiyi.cratos.eds.zabbix.auth;

import com.baiyi.cratos.eds.zabbix.param.base.BaseZbxParam;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:09
 * &#064;Version 1.0
 */
public class ZbxAuthParam {

    @Data
    @Builder
    public static class LoginParam implements BaseZbxParam.BaseRequest {

        private final String jsonrpc = "2.0";
        @Builder.Default
        private Map<String, String> params = Maps.newHashMap();
        @Builder.Default
        private String method = "user.login";
        private String auth;
        @Builder.Default
        private Integer id = 1;

        public void setUsername(String username) {
            params.put("username", username);
        }

        public void setPassword(String password) {
            params.put("password", password);
        }
    }

}
