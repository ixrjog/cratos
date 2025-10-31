package com.baiyi.cratos.eds.zabbix.auth;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.request.ZbxAuthRequest;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxAuthService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.VERY_SHORT;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:57
 * &#064;Version 1.0
 */
@Component
public class ZbxTokenHolder {

    /**
     * 测试使用
     *
     * @param zbx
     * @return
     */
    @Cacheable(cacheNames = VERY_SHORT, key = "'V0:ZBX:TOKEN:SERVERURL:'+ #zbx.url + ':USERNAME:' + #zbx.cred.username", unless = "#result == null")
    public String getToken(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxAuthService zbxAuthService = ZbxServiceFactory.createService(zbx, ZbxAuthService.class);
        ZbxAuthRequest.Login param = ZbxAuthRequest.Login.builder()
                .build();
        param.setUsername(zbx.getCred()
                .getUsername());
        param.setPassword(zbx.getCred()
                .getPassword());
        ZbxResponse<String> response = zbxAuthService.userLogin(param);
        return response.getResult();
    }

    @Cacheable(cacheNames = VERY_SHORT, key = "'V0:ZBX:BEARER:SERVERURL:'+ #zbx.url + ':USERNAME:' + #zbx.cred.username", unless = "#result == null")
    public String getBearer(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxAuthService zbxAuthService = ZbxServiceFactory.createService(zbx, ZbxAuthService.class);
        ZbxAuthRequest.Login param = ZbxAuthRequest.Login.builder()
                .build();
        param.setUsername(zbx.getCred()
                .getUsername());
        param.setPassword(zbx.getCred()
                .getPassword());
        // Bearer
        ZbxResponse<String> response = zbxAuthService.userLogin(param);
        return "Bearer " + response.getResult();
    }

}
