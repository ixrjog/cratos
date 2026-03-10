package com.baiyi.cratos.eds.sre.bridge.service;

import com.baiyi.cratos.domain.model.SreBridgeModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 14:46
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface SreBridgeService {

    @PostExchange("/basecamp/v1/open/collectEvent")
    SreBridgeModel.Result collectEvent(@RequestBody SreBridgeModel.Event event);

}