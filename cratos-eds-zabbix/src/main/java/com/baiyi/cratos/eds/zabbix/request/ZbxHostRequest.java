package com.baiyi.cratos.eds.zabbix.request;

import com.baiyi.cratos.eds.zabbix.annotation.ZbxRequestMethod;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;
import com.baiyi.cratos.eds.zabbix.request.base.BaseZbxRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:38
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxHostRequest {

    @SuperBuilder(toBuilder = true)
    @ZbxRequestMethod(group = ZbxAPIGroup.HOST, action = com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction.GET)
    public static class GetHost extends BaseZbxRequest.DefaultRequest {
    }

    @SuperBuilder(toBuilder = true)
    @ZbxRequestMethod(group = ZbxAPIGroup.HOSTPROTOTYPE, action = com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction.GET)
    public static class GetHostPrototype extends BaseZbxRequest.DefaultRequest {
    }

}
