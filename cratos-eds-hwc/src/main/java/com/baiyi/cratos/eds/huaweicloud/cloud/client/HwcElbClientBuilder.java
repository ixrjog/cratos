package com.baiyi.cratos.eds.huaweicloud.cloud.client;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.huaweicloud.cloud.util.HwcProjectUtils;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.elb.v3.ElbClient;
import com.huaweicloud.sdk.elb.v3.region.ElbRegion;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 10:43
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcElbClientBuilder {

    public static ElbClient buildElbClient(String regionId, EdsHwcConfigModel.Hwc huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = ElbRegion.valueOf(regionId);
        BasicCredentials credentials = new BasicCredentials().withAk(huaweicloud.getCred()
                                                                             .getAccessKey())
                .withSk(huaweicloud.getCred()
                                .getSecretKey())
                .withProjectId(HwcProjectUtils.findProjectId(regionId, huaweicloud));
        return ElbClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(credentials)
                .withRegion(region)
                .build();
    }

}
