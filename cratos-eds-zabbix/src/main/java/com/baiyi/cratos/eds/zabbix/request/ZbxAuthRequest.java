package com.baiyi.cratos.eds.zabbix.request;

import com.baiyi.cratos.eds.zabbix.annotation.ZbxParamMethod;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;
import com.baiyi.cratos.eds.zabbix.request.base.BaseZbxRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:09
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxAuthRequest {

    @SuperBuilder(toBuilder = true)
    @ZbxParamMethod(group = ZbxAPIGroup.USER, action = ZbxAPIAction.LOGIN)
    public static class Login extends BaseZbxRequest.DefaultRequest {

        public void setUsername(String username) {
            putParam("username", username);
        }

        public void setPassword(String password) {
            putParam("password", password);
        }
    }

}
