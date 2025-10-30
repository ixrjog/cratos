package com.baiyi.cratos.eds.zabbix.util;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.param.base.BaseZbxParam;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxInfoService;
import com.baiyi.cratos.eds.zabbix.service.ZbxServiceFactory;
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
        BaseZbxParam.InfoVersionParam param = BaseZbxParam.InfoVersionParam.builder()
                .build();
        ZbxResponse<String> response = zbxService.getVersion(param);
        return response.getResult();
    }

}