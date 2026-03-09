package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudFlareCert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudFlareService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/18 15:01
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface CloudFlareCertificateService extends CloudFlareService {

    @GetExchange("/zones/{zoneId}/ssl/certificate_packs")
    CloudFlareHttpResult<List<CloudFlareCert.Result>> listCertificatePacks(@PathVariable String zoneId,
                                                                           @RequestParam Map<String, String> param);

    /**
     * https://developers.cloudflare.com/api/resources/custom_certificates/methods/get/
     * @param zoneId
     * @param param
     * @return
     */
    @PostExchange("/zones/{zoneId}/custom_certificates")
    CloudFlareHttpResult<CloudFlareCert.CustomCertificate> createSSLConfiguration(@PathVariable String zoneId,
                                                                                  @RequestParam Map<String, String> param);

}
