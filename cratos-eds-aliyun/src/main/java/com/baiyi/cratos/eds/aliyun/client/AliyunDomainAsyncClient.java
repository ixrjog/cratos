package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.domain20180129.AsyncClient;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/26 上午10:19
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunDomainAsyncClient {

    private static final String ENDPOINT_OVERRIDE = "domain.aliyuncs.com";

    public static AsyncClient createClient(EdsAliyunConfigModel.Aliyun aliyun) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                .accessKeySecret(aliyun.getCred()
                        .getAccessKeySecret())
                .build());
        final String endpoint = Optional.of(aliyun)
                .map(EdsAliyunConfigModel.Aliyun::getDomain)
                .map(EdsAliyunConfigModel.Domain::getEndpoint)
                .orElse(ENDPOINT_OVERRIDE);
        return AsyncClient.builder()
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(ClientOverrideConfiguration.create()
                        // Endpoint 请参考 https://api.aliyun.com/product/Domain
                        .setEndpointOverride(endpoint)
                        .setConnectTimeout(Duration.ofSeconds(30)))
                .build();
    }

}
