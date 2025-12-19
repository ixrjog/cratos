package com.baiyi.cratos.eds.cloudflare.service;

import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.param.CloudflareDnsParam;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudflareService;
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
public interface CloudflareDnsService extends CloudflareService {

    @GetExchange("/zones/{zoneId}/dns_records")
    CloudflareHttpResult<List<CloudflareDns.DnsRecord>> listDnsRecords(@PathVariable String zoneId,
                                                                       @RequestParam Map<String, String> param);

    @PostExchange("/zones/{zoneId}/dns_records")
    CloudflareHttpResult<CloudflareDns.DnsRecord> createDnsRecord(@PathVariable String zoneId,
                                                                  @RequestBody CloudflareDnsParam.CreateDnsRecord createDnsRecord);

    @PatchExchange("/zones/{zoneId}/dns_records/{dnsRecordId}")
    CloudflareHttpResult<CloudflareDns.DnsRecord> updateDnsRecord(@PathVariable String zoneId,
                                                                  @PathVariable String dnsRecordId,
                                                                  @RequestBody CloudflareDnsParam.UpdateDnsRecord updateDnsRecord);

    @DeleteExchange("/zones/{zoneId}/dns_records/{dnsRecordId}")
    CloudflareHttpResult<String> deleteDnsRecord(@PathVariable String zoneId, @PathVariable String dnsRecordId);

}
