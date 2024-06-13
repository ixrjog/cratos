package com.baiyi.cratos.eds.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/6 11:11
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AmazonEc2Service {

    public static AmazonEC2 buildAmazonEC2(EdsAwsConfigModel.Aws aws) {
        return buildAmazonEC2(aws.getRegionId(), aws);
    }

    public static AmazonEC2 buildAmazonEC2(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        // AWSCredentials credentials = awsCore.getAWSCredentials();
        return AmazonEC2ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
