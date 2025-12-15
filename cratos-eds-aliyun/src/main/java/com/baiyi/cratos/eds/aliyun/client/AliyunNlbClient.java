package com.baiyi.cratos.eds.aliyun.client;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/4 15:13
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunNlbClient {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static com.aliyun.nlb20220430.Client createClient(String endpoint,
                                                             EdsConfigs.Aliyun aliyun) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred()
                        .getAccessKeySecret());
        config.endpoint = endpoint;
        return new com.aliyun.nlb20220430.Client(config);
    }

}
