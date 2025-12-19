package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudflareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareCertificateService;
import com.baiyi.cratos.eds.cloudflare.util.PageParamUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:37
 * @Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudflareCertRepo {

    public static List<CloudflareCert.Result> listCertificatePacks(EdsConfigs.Cloudflare config, String zoneId) {
        List<CloudflareCert.Result> results = Lists.newArrayList();
        int page = 1;
        CloudflareCertificateService cloudflareService = CloudflareServiceFactory.createCertificateService(config);
        while (true) {
            CloudflareHttpResult<List<CloudflareCert.Result>> rt = cloudflareService.listCertificatePacks(
                    zoneId, PageParamUtils.newPage(page));
            results.addAll(rt.getResult());
            if (results.size() == rt.getResultInfo()
                    .getTotalCount()) {
                return results;
            }
            page++;
        }
    }

}
