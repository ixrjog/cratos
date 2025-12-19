package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudflareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareDnsService;
import com.baiyi.cratos.eds.cloudflare.util.PageParamUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/9 09:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudflareDnsRepo {

    // 代理
    public static final boolean PROXIED = true;
    // 回源
    public static final boolean DIRECT = false;


    public static List<CloudflareDns.DnsRecord> listDnsRecords(EdsConfigs.Cloudflare config, String zoneId) {
        List<CloudflareDns.DnsRecord> results = Lists.newArrayList();
        int page = 1;
        int total = Integer.MAX_VALUE;
        CloudflareDnsService cloudflareDnsService = CloudflareServiceFactory.createDnsService(config);
        do {
            CloudflareHttpResult<List<CloudflareDns.DnsRecord>> rt = cloudflareDnsService.listDnsRecords(
                    zoneId,
                    PageParamUtils.newPage(
                            page)
            );
            results.addAll(rt.getResult());
            if (rt.getResultInfo() != null) {
                total = rt.getResultInfo()
                        .getTotalCount();
            }
            page++;
        } while (results.size() < total);
        return results;
    }

    public static CloudflareHttpResult<CloudflareDns.DnsRecord> createDnsRecord(EdsConfigs.Cloudflare config,
                                                                                String zoneId, String name, Long ttl,
                                                                                String type, String content,
                                                                                Boolean proxied, String comment) {
        CloudflareDnsService cloudflareDnsService = CloudflareServiceFactory.createDnsService(config);
        Map<String, Object> param = Map.ofEntries(
                entry("name", name), entry("ttl", ttl), entry("type", type), entry("content", content),
                entry("proxied", proxied), entry("comment", comment)
        );
        return cloudflareDnsService.createDnsRecord(zoneId, param);
    }

    public static CloudflareHttpResult<CloudflareDns.DnsRecord> updateDnsRecord(EdsConfigs.Cloudflare config,
                                                                                String zoneId, String dnsRecordId,
                                                                                String name, Long ttl, String type,
                                                                                String content, Boolean proxied,
                                                                                String comment) {
        CloudflareDnsService cloudflareDnsService = CloudflareServiceFactory.createDnsService(config);
        Map<String, Object> param = Map.ofEntries(
                entry("name", name), entry("ttl", ttl), entry("type", type), entry("content", content),
                entry("proxied", proxied), entry("comment", comment)
        );
        return cloudflareDnsService.updateDnsRecord(zoneId, dnsRecordId, param);
    }

    public static CloudflareHttpResult<String> deleteDnsRecord(EdsConfigs.Cloudflare config, String zoneId,
                                                               String dnsRecordId) {
        CloudflareDnsService cloudflareDnsService = CloudflareServiceFactory.createDnsService(config);
        return cloudflareDnsService.deleteDnsRecord(zoneId, dnsRecordId);
    }

}
