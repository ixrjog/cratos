package com.baiyi.cratos.eds.huaweicloud.client;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.obs.v1.ObsClient;
import com.huaweicloud.sdk.obs.v1.ObsCredentials;
import com.huaweicloud.sdk.obs.v1.region.ObsRegion;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 16:17
 * &#064;Version 1.0
 */
public class HwcObsClientBuilder {

    public static final Region AF_SOUTH_1_LOS1A = new Region("af-south-1-los1a",
            "https://obs.af-south-1-los1a.myhuaweicloud.com");

    public static ObsClient buildObsClient(String regionId, EdsHwcConfigModel.Hwc huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = "af-south-1-los1a".equals(regionId) ? AF_SOUTH_1_LOS1A : ObsRegion.valueOf(regionId);
        // 创建认证
        ObsCredentials obsCredentials = new ObsCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey());

        return ObsClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(obsCredentials)
                .withRegion(region)
                .build();
    }

}
