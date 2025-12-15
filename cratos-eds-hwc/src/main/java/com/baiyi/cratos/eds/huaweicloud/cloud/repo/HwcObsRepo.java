package com.baiyi.cratos.eds.huaweicloud.cloud.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.huaweicloud.cloud.client.HwcObsClientBuilder;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.obs.v1.ObsClient;
import com.huaweicloud.sdk.obs.v1.model.CreateBucketRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 16:19
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HwcObsRepo {

    public static void createBucket(String regionId, EdsConfigs.Hwc huaweicloud,
                                    String bucketName) throws ServiceResponseException {
        ObsClient client = HwcObsClientBuilder.buildObsClient(regionId, huaweicloud);
        CreateBucketRequest request = new CreateBucketRequest();
        request.setBucketName(bucketName);
        client.createBucket(request);
    }

}
