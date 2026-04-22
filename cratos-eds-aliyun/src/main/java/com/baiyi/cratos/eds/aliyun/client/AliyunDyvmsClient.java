package com.baiyi.cratos.eds.aliyun.client;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/22 17:06
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunDyvmsClient {

    public static com.aliyun.dyvmsapi20170525.Client createClient(EdsConfigs.Aliyun aliyun) throws Exception {
        // https://next.api.aliyun.com/api/Dyvmsapi/2017-05-25/CloudUpdateTask?spm=api-workbench.SDK%20Document.0.0.68052bf64ADkri&tab=DEMO&lang=JAVA&sdkStyle=dara&RegionId=eu-central-1
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config().setAccessKeyId(
                        aliyun.getCred()
                                .getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred()
                                            .getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dyvmsapi
        config.endpoint = "dyvmsapi.aliyuncs.com";
        return new com.aliyun.dyvmsapi20170525.Client(config);
    }

}
