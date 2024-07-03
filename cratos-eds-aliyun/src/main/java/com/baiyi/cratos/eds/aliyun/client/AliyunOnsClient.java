package com.baiyi.cratos.eds.aliyun.client;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 上午9:52
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunOnsClient {

    public static String toEndpoint(@NonNull String regionId) {
        return StringFormatter.format("rocketmq.{}.aliyuncs.com", regionId);
    }

    public static com.aliyun.rocketmq20220801.Client createV5Client(@NonNull String endpoint,
                                                                    @NonNull EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config().setAccessKeyId(
                        aliyun.getCred()
                                .getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred()
                        .getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/RocketMQ
        config.endpoint = endpoint;
        return new com.aliyun.rocketmq20220801.Client(config);
    }

}
