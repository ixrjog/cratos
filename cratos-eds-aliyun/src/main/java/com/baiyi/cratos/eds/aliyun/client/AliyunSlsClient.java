package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.openservices.log.Client;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/20 上午10:16
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunSlsClient {

    public static Client createClient(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) {
        return new Client(endpoint, aliyun.getCred()
                .getAccessKeyId(), aliyun.getCred()
                .getAccessKeySecret());
    }

}
