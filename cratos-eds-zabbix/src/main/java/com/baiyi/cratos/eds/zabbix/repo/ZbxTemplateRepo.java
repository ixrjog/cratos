package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.param.ZbxTemplateParam;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxTemplateResult;
import com.baiyi.cratos.eds.zabbix.service.ZbxServiceFactory;
import com.baiyi.cratos.eds.zabbix.service.ZbxTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 17:59
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ZbxTemplateRepo {

    private final ZbxTokenHolder zbxTokenHolder;

    public List<ZbxTemplateResult.Template> listTemplate(EdsZabbixConfigModel.Zabbix zbx) {
        ZbxTemplateService zbxService = ZbxServiceFactory.createService(zbx, ZbxTemplateService.class);
        ZbxTemplateParam.GetTemplateParam param = ZbxTemplateParam.GetTemplateParam.builder()
                .build();
        ZbxResponse<List<ZbxTemplateResult.Template>> response = zbxService.getTemplate(zbxTokenHolder.getBearer(zbx), param);
        return response.getResult();
    }

}