package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.request.ZbxUserRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxUserResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxUserService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/21 16:56
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxUserRepo {

    public static List<ZbxUserResult.User> listUser(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxUserService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxUserService.class);
        Map<String, Object> params = Map.ofEntries(entry("output", "extend"));
        ZbxUserRequest.GetUser request = ZbxUserRequest.GetUser.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxUserResult.User>> response = zbxService.getUser(request);
        return response.getResult();
    }

}