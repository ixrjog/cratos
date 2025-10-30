package com.baiyi.cratos.eds.zabbix.service;

import com.baiyi.cratos.eds.zabbix.param.ZbxTemplateParam;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxTemplateResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 17:45
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json-rpc", contentType = "application/json")
public interface ZbxTemplateService extends BaseZbxService {

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<List<ZbxTemplateResult.Template>> getTemplate(@RequestHeader("Authorization") String bearer,
                                                              @RequestBody ZbxTemplateParam.GetTemplateParam param);

}