package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.model.Zone;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:47
 * @Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudflareZoneService {

    @GetExchange("/zones")
    CloudflareHttpResult<List<Zone.Result>> listZones(@RequestHeader("Authorization") String bearerToken);

}
