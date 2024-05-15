package com.baiyi.cratos.eds.aws.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午1:48
 * &#064;Version 1.0
 */
public class AmazonSnsService {

    private AmazonSnsService() {
    }

    public static AmazonSNS buildAmazonSNS(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonSNSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
