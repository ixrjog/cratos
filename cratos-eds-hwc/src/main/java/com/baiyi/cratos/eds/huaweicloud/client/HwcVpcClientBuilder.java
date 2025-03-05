package com.baiyi.cratos.eds.huaweicloud.client;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.util.HwcProjectUtils;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.vpc.v2.VpcClient;
import com.huaweicloud.sdk.vpc.v2.region.VpcRegion;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 10:16
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcVpcClientBuilder {

    public static VpcClient buildVpcClient(String regionId, EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = VpcRegion.valueOf(regionId);
        BasicCredentials credentials = new BasicCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey())
                .withProjectId(HwcProjectUtils.findProjectId(regionId, huaweicloud));
        return VpcClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(credentials)
                .withRegion(region)
                .build();
    }

}
