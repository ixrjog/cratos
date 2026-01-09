package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudFlareService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/18 15:50
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudFlareZoneService extends CloudFlareService {

    @GetExchange("/zones")
    CloudFlareHttpResult<List<CloudFlareZone.Zone>> listZones(@RequestParam Map<String, String> param);

}
