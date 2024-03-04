package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.model.base.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.model.base.VerifyUserTokens;
import com.baiyi.cratos.eds.cloudflare.repo.base.BaseCloudflareRepo;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/1 17:23
 * @Version 1.0
 */
@Component
public class CloudflareRepo extends BaseCloudflareRepo {

    @Resource
    private CloudflareService cloudflareService;

    public CloudflareHttpResult<VerifyUserTokens.Result> verifyUserTokens(EdsCloudflareConfigModel.Cloudflare cloudflare) {
        return cloudflareService.verifyUserTokens(generateBearer(cloudflare));
    }

}
