package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.Cert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
    CloudflareHttpResult<List<Cert.Result>> listCertificatePacks(@RequestHeader("Authorization") String bearerToken, @PathVariable String zoneId);

}
