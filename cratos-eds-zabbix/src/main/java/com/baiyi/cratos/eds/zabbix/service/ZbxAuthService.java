package com.baiyi.cratos.eds.zabbix.service;

import com.baiyi.cratos.eds.zabbix.auth.ZbxAuthParam;
import com.baiyi.cratos.eds.zabbix.reslut.ZbxResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:25
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json-rpc", contentType = "application/json")
public interface ZbxAuthService extends BaseZbxService {

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<String> userLogin(@RequestBody ZbxAuthParam.LoginParam param);

}

