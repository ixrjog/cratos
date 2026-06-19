package com.baiyi.cratos.eds.aliyun.client;

import com.baiyi.cratos.eds.core.config.EdsConfigs;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/18 16:29
 * &#064;Version 1.0
 */
public class AliyunCSClient {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static com.aliyun.cs20151215.Client createClient(EdsConfigs.Aliyun aliyun) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config().setAccessKeyId(
                        aliyun.getCred()
                                .getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred()
                                            .getAccessKeySecret());
        return new com.aliyun.cs20151215.Client(config);
    }


}
