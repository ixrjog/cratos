package com.baiyi.cratos.eds.huaweicloud.client;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.huaweicloud.client.cred.HuaweicloudCredentialsBuilder;
import com.huaweicloud.sdk.ccm.v1.CcmClient;
import com.huaweicloud.sdk.ccm.v1.region.CcmRegion;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/21 16:38
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcCcmClientBuilder {

    public static CcmClient buildCcmClient(String regionId, EdsHwcConfigModel.Hwc huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = CcmRegion.valueOf(regionId);
        return CcmClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(HuaweicloudCredentialsBuilder.build(huaweicloud))
                .withRegion(region)
                .build();
    }

}
