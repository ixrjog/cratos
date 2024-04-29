package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.teaopenapi.models.Config;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;

/**
 * @Author baiyi
 * @Date 2024/2/26 11:36
 * @Version 1.0
 */
public class AliyunOpenapiClient {

    public static com.aliyun.cas20200407.Client createClient(EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new Config()
                .setRegionId(aliyun.getRegionId())
                .setAccessKeyId(aliyun.getCred().getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred().getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/cas
        config.endpoint = "cas.aliyuncs.com";
        return new com.aliyun.cas20200407.Client(config);
    }

}
