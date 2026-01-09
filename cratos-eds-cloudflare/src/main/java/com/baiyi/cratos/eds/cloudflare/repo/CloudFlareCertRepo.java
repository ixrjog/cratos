package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.CloudFlareServiceFactory;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareCert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudFlareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareCertificateService;
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
public class CloudFlareCertRepo {

    public static List<CloudFlareCert.Result> listCertificatePacks(EdsConfigs.Cloudflare config, String zoneId) {
        List<CloudFlareCert.Result> results = Lists.newArrayList();
        int page = 1;
        CloudFlareCertificateService cloudflareService = CloudFlareServiceFactory.createCertificateService(config);
        while (true) {
            CloudFlareHttpResult<List<CloudFlareCert.Result>> rt = cloudflareService.listCertificatePacks(
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
