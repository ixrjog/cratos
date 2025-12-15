package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/19 下午2:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunOssClient {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static OSS createClient(String endpoint, EdsConfigs.Aliyun aliyun) {
        return new OSSClientBuilder().build(endpoint, aliyun.getCred()
                .getAccessKeyId(), aliyun.getCred()
                .getAccessKeySecret());
    }

}