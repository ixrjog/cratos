package com.baiyi.cratos.eds.zabbix.service;

import com.baiyi.cratos.eds.zabbix.request.ZbxHostRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.base.BaseZbxService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:37
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json-rpc", contentType = "application/json")
public interface ZbxHostService extends BaseZbxService {

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<List<ZbxHostResult.Host>> getHost(@RequestBody ZbxHostRequest.GetHost request);

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<List<ZbxHostResult.HostExtend>> getHostExtend(@RequestBody ZbxHostRequest.GetHost request);

}
