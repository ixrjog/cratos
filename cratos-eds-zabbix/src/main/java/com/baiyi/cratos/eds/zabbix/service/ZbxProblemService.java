package com.baiyi.cratos.eds.zabbix.service;

import com.baiyi.cratos.eds.zabbix.request.ZbxProblemRequest;
import com.baiyi.cratos.eds.zabbix.result.ZbxProblemResult;
import com.baiyi.cratos.eds.zabbix.result.base.ZbxResponse;
import com.baiyi.cratos.eds.zabbix.service.base.BaseZbxService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/3 17:02
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json-rpc", contentType = "application/json")
public interface ZbxProblemService extends BaseZbxService {

    @PostExchange("/api_jsonrpc.php")
    ZbxResponse<List<ZbxProblemResult.Problem>> getProblem(@RequestBody ZbxProblemRequest.GetProblem request);

    @PostExchange("/api_jsonrpc.php")
    String getProblemTest(@RequestBody ZbxProblemRequest.GetProblem request);

}