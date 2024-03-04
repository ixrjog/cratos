package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.cloudflare.model.Cert;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.repo.base.BaseCloudflareRepo;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareCertService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/4 14:37
 * @Version 1.0
 */
@Component
public class CloudflareCertRepo extends BaseCloudflareRepo {

    @Resource
    private CloudflareCertService certService;

    public List<Cert.Result> listCertificatePacks(EdsCloudflareConfigModel.Cloudflare cloudflare, String zoneId) {
        List<Cert.Result> results = Lists.newArrayList();
        int page = 1;
        while (true) {
            Map<String, String> param = DictBuilder.newBuilder()
                    .put("page", String.valueOf(page))
                    .build();
            CloudflareHttpResult<List<Cert.Result>> rt = certService.listCertificatePacks(generateBearer(cloudflare), zoneId, param);
            results.addAll(rt.getResult());
            if (results.size() == rt.getResultInfo()
                    .getTotalCount()) {
                return results;
            }
            page++;
        }
    }

}
