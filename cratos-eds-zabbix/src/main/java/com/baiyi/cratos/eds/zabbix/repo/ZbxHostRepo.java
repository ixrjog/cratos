package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.request.ZbxHostRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxMapResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxHostService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:41
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxHostRepo {

    public static List<ZbxHostResult.Host> listHost(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxHostService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxHostService.class);
        ZbxHostRequest.GetHost request = ZbxHostRequest.GetHost.builder()
                .build();
        ZbxResponse<List<ZbxHostResult.Host>> response = zbxService.getHost(request);
        return response.getResult();
    }

    public static List<String> listHostIds(EdsZabbixConfigModel.Zabbix zbx, Map<String, Object> params) {
        ZbxHostService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxHostService.class);
        ZbxHostRequest.GetHost request = ZbxHostRequest.GetHost.builder()
                .params(params)
                .build();
        ZbxMapResponse<ZbxHostResult.Host> response = zbxService.listHostIds(request);
        Map<String, ZbxHostResult.Host> resultMap = response.getResult();
        if (resultMap == null || resultMap.isEmpty()) {
            return List.of();
        }
        return resultMap.values()
                .stream()
                .map(ZbxHostResult.Host::getHostid)
                .collect(Collectors.toList());
    }

    public static ZbxHostResult.HostExtend getHostExtend(EdsZabbixConfigModel.Zabbix zbx, String hostid) {
        ZbxHostService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxHostService.class);
        Map<String, Object> params = Map.ofEntries(entry("output", "extend"),
                entry("selectParentTemplates", List.of("templateid", "name")), entry("selectHostGroups", "extend"),
                entry("selectInterfaces", "extend"), entry("hostids", hostid));
        ZbxHostRequest.GetHost request = ZbxHostRequest.GetHost.builder()
                .params(params)
                .build();
        ZbxResponse<List<ZbxHostResult.HostExtend>> response = zbxService.getHostExtend(request);
        return CollectionUtils.isEmpty(response.getResult()) ? null : response.getResult()
                .getFirst();
    }

}
