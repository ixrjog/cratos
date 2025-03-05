package com.baiyi.cratos.eds.huaweicloud.client;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 下午1:51
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcIamClientBuilder {

    public static IamClient buildIamClient(String regionId, EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = IamRegion.valueOf(regionId);
        GlobalCredentials globalCredentials = new GlobalCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey())
                .withDomainId(huaweicloud.getCred()
                        .getUid());
        return IamClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(globalCredentials)
                .withRegion(region)
                .build();
    }

}
