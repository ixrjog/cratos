package com.baiyi.cratos.eds.huaweicloud.client;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.util.HwcProjectUtil;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 下午1:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcEcsClientBuilder {

    public static EcsClient buildEcsClient(String regionId, EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);
        Region region = EcsRegion.valueOf(regionId);

        // 创建认证
        BasicCredentials credentials = new BasicCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey())
                .withProjectId(HwcProjectUtil.findProjectId(regionId, huaweicloud));
        return EcsClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(credentials)
                .withRegion(region)
                .build();
    }

}
