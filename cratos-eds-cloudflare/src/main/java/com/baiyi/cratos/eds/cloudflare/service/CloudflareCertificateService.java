package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudflareService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/18 15:01
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudflareCertificateService extends CloudflareService {

    @GetExchange("/zones/{zoneId}/ssl/certificate_packs")
    CloudflareHttpResult<List<CloudflareCert.Result>> listCertificatePacks(@PathVariable String zoneId,
                                                                           @RequestParam Map<String, String> param);

}
