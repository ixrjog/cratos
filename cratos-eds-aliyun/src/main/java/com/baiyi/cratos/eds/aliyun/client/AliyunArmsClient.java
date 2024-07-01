package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.arms20190808.AsyncClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/1 上午10:24
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunArmsClient {

    private static final String ENDPOINT = "arms.cn-hangzhou.aliyuncs.com";

    private static final String REGION = "cn-hangzhou";

    public static final Long MAX_RESULTS = 200L;

    public static final String MAX_RESULTS_S = String.valueOf(MAX_RESULTS);

    /**
     * 代码参考
     * https://next.api.aliyun.com/api-tools/sdk/ARMS?version=2019-08-08&language=java-async-tea
     * <p>
     * 接入点参考
     * https://help.aliyun.com/document_detail/441765.html?spm=a2c4g.441908.0.0.8800710dLdFZHW
     *
     * @param regionId
     * @param arms
     * @return
     */
    public static AsyncClient buildAsyncClient(String regionId, EdsAliyunConfigModel.Aliyun aliyun) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                .accessKeySecret(aliyun.getCred()
                        .getAccessKeySecret())
                .build());

        final String region = StringUtils.isNotBlank(regionId) ? regionId : REGION;

        final String endpoint = Optional.of(aliyun)
                .map(EdsAliyunConfigModel.Aliyun::getArms)
                .map(EdsAliyunConfigModel.ARMS::getEndpoint)
                .orElse(ENDPOINT);

        ClientOverrideConfiguration clientOverrideConfiguration = ClientOverrideConfiguration.create()
                .setEndpointOverride(endpoint)
                .setConnectTimeout(Duration.ofSeconds(30));

        return AsyncClient.builder()
                // Region ID
                .region(region)
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                .overrideConfiguration(clientOverrideConfiguration)
                .build();
    }

}
