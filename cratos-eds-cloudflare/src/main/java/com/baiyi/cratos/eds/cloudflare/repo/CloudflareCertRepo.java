package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:37
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CloudflareCertRepo {

    private final CloudflareService cloudflareService;

    public List<CloudflareCert.Result> listCertificatePacks(EdsCloudflareConfigModel.Cloudflare cloudflare,
                                                            String zoneId) {
        List<CloudflareCert.Result> results = Lists.newArrayList();
        int page = 1;
        while (true) {
            Map<String, String> param = DictBuilder.newBuilder()
                    .put("page", String.valueOf(page))
                    .build();
            CloudflareHttpResult<List<CloudflareCert.Result>> rt = cloudflareService.listCertificatePacks(cloudflare.getCred()
                    .toBearer(), zoneId, param);
            results.addAll(rt.getResult());
            if (results.size() == rt.getResultInfo()
                    .getTotalCount()) {
                return results;
            }
            page++;
        }
    }

}
