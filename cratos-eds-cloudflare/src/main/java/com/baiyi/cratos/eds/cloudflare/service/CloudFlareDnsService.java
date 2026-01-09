package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudFlareDns;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.param.CloudFlareDnsParam;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudFlareService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:38
 * @Version 1.0
 */
@HttpExchange(accept = "application/json", contentType = "application/json")
public interface CloudFlareDnsService extends CloudFlareService {

    @GetExchange("/zones/{zoneId}/dns_records")
    CloudFlareHttpResult<List<CloudFlareDns.DnsRecord>> listDnsRecords(@PathVariable String zoneId,
                                                                       @RequestParam Map<String, String> param);

    @PostExchange("/zones/{zoneId}/dns_records")
    CloudFlareHttpResult<CloudFlareDns.DnsRecord> createDnsRecord(@PathVariable String zoneId,
                                                                  @RequestBody CloudFlareDnsParam.CreateDnsRecord createDnsRecord);

    @PatchExchange("/zones/{zoneId}/dns_records/{dnsRecordId}")
    CloudFlareHttpResult<CloudFlareDns.DnsRecord> updateDnsRecord(@PathVariable String zoneId,
                                                                  @PathVariable String dnsRecordId,
                                                                  @RequestBody CloudFlareDnsParam.UpdateDnsRecord updateDnsRecord);

    @DeleteExchange("/zones/{zoneId}/dns_records/{dnsRecordId}")
    CloudFlareHttpResult<String> deleteDnsRecord(@PathVariable String zoneId, @PathVariable String dnsRecordId);

}
