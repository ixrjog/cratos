package com.baiyi.cratos.eds.aliyun.client;

import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;

/**
 * @Author baiyi
 * @Date 2024/4/26 上午10:18
 * @Version 1.0
 */
public class AliyunAlbClient {

    private AliyunAlbClient() {
    }

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static com.aliyun.alb20200616.Client createClient(String endpoint,
                                                             EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(aliyun.getCred()
                        .getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Alb
        // alb.cn-hangzhou.aliyuncs.com
        // alb.eu-central-1.aliyuncs.com
        config.endpoint = endpoint;
        return new com.aliyun.alb20200616.Client(config);
    }

}
