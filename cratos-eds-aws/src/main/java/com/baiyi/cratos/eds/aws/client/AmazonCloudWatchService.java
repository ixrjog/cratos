package com.baiyi.cratos.eds.aws.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;

/**
 * @Author baiyi
 * @Date 2024/4/1 14:20
 * @Version 1.0
 */
public class AmazonCloudWatchService {

    private AmazonCloudWatchService() {
    }

    public static AmazonCloudWatch buildAmazonCloudWatch(EdsAwsConfigModel.Aws aws) {
        return buildAmazonCloudWatch(aws.getRegionId(), aws);
    }

    public static AmazonCloudWatch buildAmazonCloudWatch(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonCloudWatchClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
