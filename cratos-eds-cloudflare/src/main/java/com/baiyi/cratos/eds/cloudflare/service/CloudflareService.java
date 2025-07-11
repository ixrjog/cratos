package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:38
 * @Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudflareService {

    @GetExchange("/zones/{zoneId}/ssl/certificate_packs")
    CloudflareHttpResult<List<CloudflareCert.Result>> listCertificatePacks(@RequestHeader("Authorization") String bearerToken, @PathVariable String zoneId, @RequestParam Map<String, String> param);

    @GetExchange("/zones")
    CloudflareHttpResult<List<CloudflareZone.Zone>> listZones(@RequestHeader("Authorization") String bearerToken, @RequestParam Map<String, String> param);

    @GetExchange("/zones/{zoneId}/dns_records")
    CloudflareHttpResult<List<CloudflareDns.DnsRecord>> listDnsRecords(@RequestHeader("Authorization") String bearerToken, @PathVariable String zoneId, @RequestParam Map<String, String> param);

}
