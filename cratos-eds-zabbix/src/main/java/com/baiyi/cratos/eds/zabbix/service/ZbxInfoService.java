package com.baiyi.cratos.eds.zabbix.service;

import com.baiyi.cratos.eds.zabbix.request.base.BaseZbxRequest;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.base.BaseZbxService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 18:28
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json-rpc", contentType = "application/json")
public interface ZbxInfoService extends BaseZbxService {

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<String> getVersion(@RequestBody BaseZbxRequest.InfoVersion request);

}