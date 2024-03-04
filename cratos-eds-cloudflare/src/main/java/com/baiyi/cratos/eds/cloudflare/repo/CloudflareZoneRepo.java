package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.model.Zone;
import com.baiyi.cratos.eds.cloudflare.repo.base.BaseCloudflareRepo;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareZoneService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:34
 * @Version 1.0
 */
@Component
public class CloudflareZoneRepo extends BaseCloudflareRepo {

    @Resource
    private CloudflareZoneService cloudflareZoneService;

    public CloudflareHttpResult<List<Zone.Result>> listZones(EdsCloudflareConfigModel.Cloudflare cloudflare) {
        return cloudflareZoneService.listZones(generateBearer(cloudflare));
    }

}
