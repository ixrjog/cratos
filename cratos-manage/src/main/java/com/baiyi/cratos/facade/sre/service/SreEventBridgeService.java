package com.baiyi.cratos.facade.sre.service;

import com.baiyi.cratos.domain.param.http.sre.SreBridgeParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 11:00
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface SreEventBridgeService {

    @PostExchange("/basecamp/v1/open/collectEvent")
    String collectEvent(@RequestBody SreBridgeParam.Event event);

}
