package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.Zone;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:47
 * @Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudflareZoneService {

    @GetExchange("/zones")
    CloudflareHttpResult<List<Zone.Result>> listZones(@RequestHeader("Authorization") String bearerToken, @RequestParam Map<String, String> param);

}
