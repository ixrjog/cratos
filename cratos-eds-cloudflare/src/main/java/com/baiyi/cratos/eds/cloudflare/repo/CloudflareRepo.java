package com.baiyi.cratos.eds.cloudflare.repo;

import com.baiyi.cratos.eds.cloudflare.client.CloudflareService;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareHttpResult;
import com.baiyi.cratos.eds.cloudflare.model.VerifyUserTokens;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/1 17:23
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CloudflareRepo {

    private final CloudflareService cloudflareService;

    public CloudflareHttpResult<VerifyUserTokens.Result> ddd(EdsCloudflareConfigModel.Cloudflare cloudflare) {
        return cloudflareService.verifyUserTokens(generateBearer(cloudflare));
    }

    private String generateBearer(EdsCloudflareConfigModel.Cloudflare cloudflare) {
        return Joiner.on(" ")
                .join("Bearer", cloudflare.getCred()
                        .getApiToken());
    }

}
