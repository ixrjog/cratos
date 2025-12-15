package com.baiyi.cratos.eds.aliyun.client;

import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/2 上午10:53
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunDdsClient {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static com.aliyun.dds20151201.Client createClient(String endpoint,
                                                             EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(aliyun.getCred().getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred().getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dds
        config.endpoint = endpoint;
        return new com.aliyun.dds20151201.Client(config);
    }

}
