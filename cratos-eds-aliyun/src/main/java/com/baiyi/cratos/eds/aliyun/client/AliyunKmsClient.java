package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.kms20160120.AsyncClient;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/8 16:29
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunKmsClient {

    public static AsyncClient buildKmsClient(EdsAliyunConfigModel.Aliyun aliyun) {
        String endpoint = Optional.of(aliyun)
                .map(EdsAliyunConfigModel.Aliyun::getKms)
                .map(EdsAliyunConfigModel.KMS::getEndpoints)
                .map(List::getFirst)
                .orElse("kms.eu-central-1.aliyuncs.com");
        return buildKmsClient(endpoint, aliyun);
    }

    public static AsyncClient buildKmsClient(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                .accessKeySecret(aliyun.getCred()
                        .getAccessKeySecret())
                .build());
        return AsyncClient.builder()
                .credentialsProvider(provider)
                .overrideConfiguration(ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Kms
                                .setEndpointOverride(endpoint)
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();
    }

}
