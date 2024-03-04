package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.cloudflare.model.Zone;
import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.repo.base.BaseCloudflareRepo;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareZoneService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:34
 * @Version 1.0
 */
@Component
public class CloudflareZoneRepo extends BaseCloudflareRepo {

    @Resource
    private CloudflareZoneService cloudflareZoneService;

    public List<Zone.Result> listZones(EdsCloudflareConfigModel.Cloudflare cloudflare) {
        List<Zone.Result> results = Lists.newArrayList();
        int page = 1;
        while (true) {
            Map<String, String> param = DictBuilder.newBuilder()
                    .put("page", String.valueOf(page))
                    .build();
            CloudflareHttpResult<List<Zone.Result>> rt = cloudflareZoneService.listZones(generateBearer(cloudflare), param);
            results.addAll(rt.getResult());
            if (results.size() == rt.getResultInfo()
                    .getTotalCount()) {
                return results;
            }
            page++;
        }
    }

}
