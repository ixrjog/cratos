package com.baiyi.cratos.eds.aws.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.transfer.AWSTransfer;
import com.amazonaws.services.transfer.AWSTransferClientBuilder;
import com.baiyi.cratos.eds.aws.core.AwsCredentialsManager;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/13 上午9:55
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsTransferService {

    public static AWSTransfer buildAwsTransfer(String regionId, EdsAwsConfigModel.Aws aws) {
        AWSCredentials credentials = AwsCredentialsManager.buildAWSCredentials(aws);
        return AWSTransferClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(regionId)
                .build();
    }

}
