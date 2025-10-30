package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.param.ZbxHostParam;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxHostService;
import com.baiyi.cratos.eds.zabbix.service.ZbxServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:41
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ZbxHostRepo {

    private final ZbxTokenHolder zbxTokenHolder;

    public List<ZbxHostResult.Host> listHost(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxHostService zbxService = ZbxServiceFactory.createService(zbx, ZbxHostService.class);
        ZbxHostParam.GetHostParam param = ZbxHostParam.GetHostParam.builder()
                .build();
        ZbxResponse<List<ZbxHostResult.Host>> response = zbxService.getHost(zbxTokenHolder.getBearer(zbx), param);
        return response.getResult();
    }

    public ZbxHostResult.HostExtend getHostExtend(EdsZabbixConfigModel.Zabbix zbx, String hostid) {
        ZbxHostService zbxService = ZbxServiceFactory.createService(zbx, ZbxHostService.class);
        Map<String, Object> params = Map.of("output", "extend", "selectParentTemplates", List.of("templateid", "name"),
                "selectHostGroups", "extend", "hostids", hostid);
        ZbxHostParam.GetHostParam param = ZbxHostParam.GetHostParam.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxHostResult.HostExtend>> response = zbxService.getHostExtend(zbxTokenHolder.getBearer(zbx),
                param);
        return CollectionUtils.isEmpty(response.getResult()) ? null : response.getResult()
                .getFirst();
    }

}
