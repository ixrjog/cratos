package com.baiyi.cratos.eds.aws.client;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;

/**
 * @Author baiyi
 * @Date 2024/3/29 18:31
 * @Version 1.0
 */
public class AmazonElbService {

    public static AmazonElasticLoadBalancing buildAmazonELB(EdsAwsConfigModel.Aws aws) {
        return buildAmazonELB(aws.getRegionId(), aws);
    }

    public static AmazonElasticLoadBalancing buildAmazonELB(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonElasticLoadBalancingClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
