package com.baiyi.cratos.eds.zabbix.param;

import com.baiyi.cratos.eds.zabbix.annotation.ZbxParamMethod;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;
import com.baiyi.cratos.eds.zabbix.param.base.BaseZbxParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:38
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxHostParam {

    @SuperBuilder(toBuilder = true)
    @ZbxParamMethod(group = ZbxAPIGroup.HOST, action = com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction.GET)
    public static class GetHostParam extends BaseZbxParam.DefaultParam {
    }

    @SuperBuilder(toBuilder = true)
    @ZbxParamMethod(group = ZbxAPIGroup.HOSTPROTOTYPE, action = com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction.GET)
    public static class GetHostPrototypeParam extends BaseZbxParam.DefaultParam {
    }

}
