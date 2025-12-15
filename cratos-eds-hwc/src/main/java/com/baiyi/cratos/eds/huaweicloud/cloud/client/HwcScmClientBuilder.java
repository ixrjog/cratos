package com.baiyi.cratos.eds.huaweicloud.cloud.client;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.huaweicloud.cloud.util.HwcProjectUtils;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.scm.v3.ScmClient;
import com.huaweicloud.sdk.scm.v3.region.ScmRegion;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/24 15:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcScmClientBuilder {

    public static final Region EU_WEST_101 = new Region("eu-west-101", "https://scm.eu-west-101.myhuaweicloud.eu");

    public static ScmClient buildScmClient(String regionId,
                                           EdsConfigs.Hwc huaweicloud) throws IllegalArgumentException {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = "eu-west-101".equals(regionId) ? EU_WEST_101 : ScmRegion.valueOf(regionId);
        BasicCredentials credentials = new BasicCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey())
                .withProjectId(HwcProjectUtils.findProjectId(regionId, huaweicloud));
        return ScmClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(credentials)
                .withRegion(region)
                .build();
    }

}
