package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudFlareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareZoneService;
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
public class CloudFlareZoneRepo {

    public static List<CloudFlareZone.Zone> listZones(EdsConfigs.Cloudflare config) {
        List<CloudFlareZone.Zone> results = Lists.newArrayList();
        int page = 1;
        CloudFlareZoneService cloudflareService = CloudFlareServiceFactory.createZoneService(config);
        while (true) {
            CloudFlareHttpResult<List<CloudFlareZone.Zone>> rt = cloudflareService.listZones(
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
