package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudflareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareZoneService;
import com.baiyi.cratos.eds.cloudflare.util.PageParamUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:34
 * @Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudflareZoneRepo {

    public static List<CloudflareZone.Zone> listZones(EdsConfigs.Cloudflare config) {
        List<CloudflareZone.Zone> results = Lists.newArrayList();
        int page = 1;
        CloudflareZoneService cloudflareService = CloudflareServiceFactory.createZoneService(config);
        while (true) {
            CloudflareHttpResult<List<CloudflareZone.Zone>> rt = cloudflareService.listZones(
                    PageParamUtils.newPage(page));
            results.addAll(rt.getResult());
            if (results.size() == rt.getResultInfo()
                    .getTotalCount()) {
                return results;
            }
            page++;
        }
    }

}
