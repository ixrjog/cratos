package com.baiyi.cratos.eds.aws.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.route53domains.AmazonRoute53Domains;
import com.amazonaws.services.route53domains.AmazonRoute53DomainsAsyncClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午2:40
 * @Version 1.0
 */
public class AmazonRoute53DomainsService {

    private static final String GLOBAL = "us-east-1";

    private AmazonRoute53DomainsService() {
    }

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
