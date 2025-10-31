package com.baiyi.cratos.eds.zabbix.util;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.request.base.BaseZbxRequest;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxInfoService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 19:02
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxInfoUtils {

    public static String getVersion(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxInfoService zbxService = ZbxServiceFactory.createService(zbx, ZbxInfoService.class);
        BaseZbxRequest.InfoVersion request = BaseZbxRequest.InfoVersion.builder()
                .build();
        ZbxResponse<String> response = zbxService.getVersion(request);
        return response.getResult();
    }

}