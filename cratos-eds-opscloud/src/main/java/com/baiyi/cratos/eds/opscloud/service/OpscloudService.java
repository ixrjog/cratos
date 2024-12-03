package com.baiyi.cratos.eds.opscloud.service;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.eds.opscloud.param.OcApplicationParam;
import com.baiyi.cratos.eds.opscloud.vo.OcApplicationVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 10:54
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface OpscloudService {

    // Application
    @PostExchange("/api/application/page/query")
    HttpResult<DataTable<OcApplicationVO.Application>> queryApplicationPage(@RequestHeader("AccessToken") String accessToken,
                                                                            @RequestBody OcApplicationParam.ApplicationPageQuery pageQuery);

}
