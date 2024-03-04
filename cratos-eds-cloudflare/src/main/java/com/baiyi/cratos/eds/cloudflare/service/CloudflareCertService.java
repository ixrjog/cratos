package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.model.Zone;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:38
 * @Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudflareCertService {

    @GetExchange("/zones/{zoneId}/ssl/certificate_packs")
    CloudflareHttpResult<List<Zone.Result>> listCertificatePacks(@RequestHeader("Authorization") String bearerToken, @RequestParam("zoneId") String zoneId);

}
