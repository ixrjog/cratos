package com.baiyi.cratos.eds.zabbix.param;

import com.baiyi.cratos.eds.zabbix.annotation.ZbxParamMethod;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;
import com.baiyi.cratos.eds.zabbix.param.base.BaseZbxParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 10:02
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxHostGroupParam {

    @SuperBuilder(toBuilder = true)
    @ZbxParamMethod(group = ZbxAPIGroup.HOSTGROUP, action = com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction.GET)
    public static class GetHostGroupParam extends BaseZbxParam.DefaultParam {
    }

}
