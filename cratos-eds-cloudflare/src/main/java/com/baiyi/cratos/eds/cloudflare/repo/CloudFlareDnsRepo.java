package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudFlareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareDns;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.param.CloudFlareDnsParam;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareDnsService;
import com.baiyi.cratos.eds.cloudflare.util.PageParamUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/9 09:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudFlareDnsRepo {

    // 代理
    public static final boolean PROXIED = true;
    // 回源
    public static final boolean DIRECT = false;


    public static List<CloudFlareDns.DnsRecord> listDnsRecords(EdsConfigs.Cloudflare config, String zoneId) {
        List<CloudFlareDns.DnsRecord> results = Lists.newArrayList();
        int page = 1;
        int total = Integer.MAX_VALUE;
        CloudFlareDnsService cloudflareDnsService = CloudFlareServiceFactory.createDnsService(config);
        do {
            CloudFlareHttpResult<List<CloudFlareDns.DnsRecord>> rt = cloudflareDnsService.listDnsRecords(
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

    public static CloudFlareHttpResult<CloudFlareDns.DnsRecord> createDnsRecord(EdsConfigs.Cloudflare config,
                                                                                String zoneId, String name, Long ttl,
                                                                                String type, String content,
                                                                                Boolean proxied, String comment) {
        CloudFlareDnsService cloudflareDnsService = CloudFlareServiceFactory.createDnsService(config);
        CloudFlareDnsParam.CreateDnsRecord createDnsRecord = CloudFlareDnsParam.CreateDnsRecord.builder()
                .name(name)
                .ttl(ttl)
                .type(type)
                .content(content)
                .comment(comment)
                .proxied(proxied)
                .build();
        return cloudflareDnsService.createDnsRecord(zoneId, createDnsRecord);
    }

    public static CloudFlareHttpResult<CloudFlareDns.DnsRecord> updateDnsRecord(EdsConfigs.Cloudflare config,
                                                                                String zoneId, String dnsRecordId,
                                                                                String name, Long ttl, String type,
                                                                                String content, Boolean proxied,
                                                                                String comment) {
        CloudFlareDnsService cloudflareDnsService = CloudFlareServiceFactory.createDnsService(config);
        CloudFlareDnsParam.UpdateDnsRecord updateDnsRecord = CloudFlareDnsParam.UpdateDnsRecord.builder()
                .name(name)
                .ttl(ttl)
                .type(type)
                .content(content)
                .comment(comment)
                .proxied(proxied)
                .build();
        return cloudflareDnsService.updateDnsRecord(zoneId, dnsRecordId, updateDnsRecord);
    }

    public static CloudFlareHttpResult<String> deleteDnsRecord(EdsConfigs.Cloudflare config, String zoneId,
                                                               String dnsRecordId) {
        CloudFlareDnsService cloudflareDnsService = CloudFlareServiceFactory.createDnsService(config);
        return cloudflareDnsService.deleteDnsRecord(zoneId, dnsRecordId);
    }

}
