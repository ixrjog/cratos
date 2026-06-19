package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudFlareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlarePageRules;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlarePageRulesService;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 09:43
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudFlarePageRulesRepo {

    public static List<CloudFlarePageRules.PageRule> listPageRules(EdsConfigs.Cloudflare config, String zoneId) {
        CloudFlarePageRulesService cloudflarePagerulesService = CloudFlareServiceFactory.createPageRulesService(config);
        CloudFlareHttpResult<List<CloudFlarePageRules.PageRule>> rt = cloudflarePagerulesService.listPageRules(zoneId);
        return rt.getResult();
    }

}
