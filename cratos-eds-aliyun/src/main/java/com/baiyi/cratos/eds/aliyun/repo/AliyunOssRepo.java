package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.oss.model.Bucket;
import com.baiyi.cratos.eds.aliyun.client.AliyunOssClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/19 下午3:19
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunOssRepo {

    public static List<Bucket> listBuckets(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) {
        return AliyunOssClient.createClient(endpoint, aliyun)
                .listBuckets();
    }

}
