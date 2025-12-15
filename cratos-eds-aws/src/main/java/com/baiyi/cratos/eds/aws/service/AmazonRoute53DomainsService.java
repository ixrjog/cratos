package com.baiyi.cratos.eds.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.route53domains.AmazonRoute53Domains;
import com.amazonaws.services.route53domains.AmazonRoute53DomainsAsyncClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午2:40
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AmazonRoute53DomainsService {

    private static final String GLOBAL = "us-east-1";

    public static AmazonRoute53Domains buildAmazonRoute53Domains(EdsAwsConfigModel.Aws aws) {
        return buildAmazonRoute53Domains(GLOBAL, aws);
    }

    public static AmazonRoute53Domains buildAmazonRoute53Domains(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonRoute53DomainsAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
