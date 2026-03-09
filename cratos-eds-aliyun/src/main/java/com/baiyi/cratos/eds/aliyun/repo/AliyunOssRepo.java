package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GetBucketPolicyResult;
import com.baiyi.cratos.eds.aliyun.client.AliyunOssClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
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

    /**
     * 全局查询
     * @param endpoint
     * @param aliyun
     * @return
     */
    public static List<Bucket> listBuckets(String endpoint, EdsConfigs.Aliyun aliyun) {
        return AliyunOssClient.createClient(endpoint, aliyun)
                .listBuckets();
    }

    public static String getBucketPolicy(String endpoint, EdsConfigs.Aliyun aliyun, String bucketName) {
        try {
            GetBucketPolicyResult result = AliyunOssClient.createClient(endpoint, aliyun)
                    .getBucketPolicy(bucketName);
            return result.getPolicyText();
        } catch (OSSException e) {
            if ("NoSuchBucketPolicy".equals(e.getErrorCode())) {
                return null;
            }
            throw e;
        }
    }

    public static String getBucketPolicy(EdsConfigs.Aliyun aliyun, Bucket bucket) {
        try {
            GetBucketPolicyResult result = AliyunOssClient.createClient(bucket.getExtranetEndpoint(), aliyun)
                    .getBucketPolicy(bucket.getName());
            return result.getPolicyText();
        } catch (OSSException e) {
            if ("NoSuchBucketPolicy".equals(e.getErrorCode())) {
                return null;
            }
            throw e;
        }
    }

}
