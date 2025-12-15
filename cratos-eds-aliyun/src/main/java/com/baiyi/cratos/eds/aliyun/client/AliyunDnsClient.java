package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.alidns20150109.AsyncClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import darabonba.core.client.ClientOverrideConfiguration;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/9 15:48
 * &#064;Version 1.0
 */
public class AliyunDnsClient {

    public static com.aliyun.sdk.service.alidns20150109.AsyncClient createClient(
            EdsConfigs.Aliyun aliyun) throws Exception {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                                                                                    .accessKeyId(aliyun.getCred()
                                                                                                         .getAccessKeyId())
                                                                                    .accessKeySecret(aliyun.getCred()
                                                                                                             .getAccessKeySecret())
                                                                                    .build());
        return AsyncClient.builder()
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(ClientOverrideConfiguration.create()
                                               // Endpoint 请参考 https://api.aliyun.com/product/Alidns
                                               .setEndpointOverride("alidns.aliyuncs.com")
                                       //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();
    }

}
