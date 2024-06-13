package com.baiyi.cratos.eds.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.certificatemanager.AWSCertificateManager;
import com.amazonaws.services.certificatemanager.AWSCertificateManagerClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:36
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AmazonAcmService {

    public static AWSCertificateManager buildAWSCertificateManager(EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return  AWSCertificateManagerClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(aws.getRegionId())
                .build();
    }

    public static AWSCertificateManager buildAWSCertificateManager(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AWSCertificateManagerClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
