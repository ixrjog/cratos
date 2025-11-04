package com.baiyi.cratos.eds.zabbix.request;


import com.baiyi.cratos.eds.zabbix.annotation.ZbxRequestMethod;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;
import com.baiyi.cratos.eds.zabbix.request.base.BaseZbxRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/3 17:01
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxProblemRequest {

    @SuperBuilder(toBuilder = true)
    @ZbxRequestMethod(group = ZbxAPIGroup.PROBLEM, action = com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction.GET)
    public static class GetProblem extends BaseZbxRequest.DefaultRequest {
    }

}