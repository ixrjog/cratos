package com.baiyi.cratos.eds.aws.core;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:17
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsCredentialsManager {

    public static AWSCredentials buildAWSCredentials(EdsAwsConfigModel.Aws aws) {
        return new BasicAWSCredentials(aws.getCred()
                .getAccessKey(), aws.getCred()
                .getSecretKey());
    }

}
