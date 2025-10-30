package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.param.ZbxHostGroupParam;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxHostGroupResult;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxHostGroupService;
import com.baiyi.cratos.eds.zabbix.service.ZbxServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 10:04
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ZbxHostGroupRepo {

    private final ZbxTokenHolder zbxTokenHolder;

    public List<ZbxHostGroupResult.HostGroup> listHostGroup(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxHostGroupService zbxService = ZbxServiceFactory.createService(zbx, ZbxHostGroupService.class);
        ZbxHostGroupParam.GetHostGroupParam param = ZbxHostGroupParam.GetHostGroupParam.builder()
                .build();
        ZbxResponse<List<ZbxHostGroupResult.HostGroup>> response = zbxService.getHostGroup(zbxTokenHolder.getBearer(zbx), param);
        return response.getResult();
    }

}