package com.baiyi.cratos.eds.cloudflare.repo.base;

import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.google.common.base.Joiner;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:55
 * @Version 1.0
 */
public abstract class BaseCloudflareRepo {

    protected String generateBearer(EdsCloudflareConfigModel.Cloudflare cloudflare) {
        return Joiner.on(" ")
                .join("Bearer", cloudflare.getCred()
                        .getApiToken());
    }

}
