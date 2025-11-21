package com.baiyi.cratos.eds.zabbix.service;

import com.baiyi.cratos.eds.zabbix.request.ZbxUserRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxUserResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.base.BaseZbxService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/21 16:46
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json-rpc", contentType = "application/json")
public interface ZbxUserService extends BaseZbxService {

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<List<ZbxUserResult.User>> getUser(@RequestBody ZbxUserRequest.GetUser request);

}