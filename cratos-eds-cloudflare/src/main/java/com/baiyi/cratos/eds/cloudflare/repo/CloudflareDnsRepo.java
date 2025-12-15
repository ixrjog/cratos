package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareService;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/9 09:58
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CloudflareDnsRepo {

    private final CloudflareService cloudflareService;

    public List<CloudflareDns.DnsRecord> listDnsRecords(EdsConfigs.Cloudflare cloudflare, String zoneId) {
        List<CloudflareDns.DnsRecord> results = Lists.newArrayList();
        int page = 1;
        int total = Integer.MAX_VALUE;
        do {
            Map<String, String> param = DictBuilder.newBuilder()
                    .put("page", String.valueOf(page))
                    .build();
            CloudflareHttpResult<List<CloudflareDns.DnsRecord>> rt = cloudflareService.listDnsRecords(
                    cloudflare.getCred()
                            .toBearer(), zoneId, param);
            results.addAll(rt.getResult());
            if (rt.getResultInfo() != null) {
                total = rt.getResultInfo()
                        .getTotalCount();
            }
            page++;
        } while (results.size() < total);
        return results;
    }

}
