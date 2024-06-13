package com.baiyi.cratos.eds.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午1:49
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AmazonRoute53Service {

    public static AmazonRoute53 buildAmazonRoute53(EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonRoute53ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
