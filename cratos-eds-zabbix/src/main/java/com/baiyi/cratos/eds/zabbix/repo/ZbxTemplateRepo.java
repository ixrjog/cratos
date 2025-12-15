package com.baiyi.cratos.eds.zabbix.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.zabbix.request.ZbxTemplateRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxTemplateResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.ZbxTemplateService;
import com.baiyi.cratos.eds.zabbix.service.factory.ZbxServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 17:59
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxTemplateRepo {

    public static List<ZbxTemplateResult.Template> listTemplate(EdsConfigs.Zabbix zbx) {
        ZbxTemplateService zbxService = ZbxServiceFactory.createAuthenticatedService(zbx, ZbxTemplateService.class);
        ZbxTemplateRequest.GetTemplate request = ZbxTemplateRequest.GetTemplate.builder()
                .build();
        ZbxResponse<List<ZbxTemplateResult.Template>> response = zbxService.getTemplate(request);
        return response.getResult();
    }

}