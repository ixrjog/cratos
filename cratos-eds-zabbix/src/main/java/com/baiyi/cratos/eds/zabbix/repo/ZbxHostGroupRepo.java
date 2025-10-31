package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.request.ZbxHostGroupRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostGroupResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxHostGroupService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 10:04
 * &#064;Version 1.0
 */
@Component
public class ZbxHostGroupRepo {

    public List<ZbxHostGroupResult.HostGroup> listHostGroup(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxHostGroupService zbxService = ZbxServiceFactory.createService(zbx, ZbxHostGroupService.class);
        ZbxHostGroupRequest.GetHostGroup request = ZbxHostGroupRequest.GetHostGroup.builder()
                .build();
        ZbxResponse<List<ZbxHostGroupResult.HostGroup>> response = zbxService.getHostGroup(request);
        return response.getResult();
    }

}