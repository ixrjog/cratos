package com.baiyi.cratos.eds.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 15:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AmazonIdentityManagementService {

    public static AmazonIdentityManagement buildAmazonIdentityManagement(EdsConfigs.Aws aws) {
        return buildAmazonIdentityManagement(aws.getRegionId(), aws);
    }

    public static AmazonIdentityManagement buildAmazonIdentityManagement(String regionId, EdsConfigs.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AmazonIdentityManagementClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
