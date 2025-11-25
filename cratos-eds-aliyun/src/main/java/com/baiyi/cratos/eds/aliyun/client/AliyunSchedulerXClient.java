package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.schedulerx220190430.AsyncClient;
import com.aliyun.sdk.service.schedulerx220190430.models.GetJobInfoRequest;
import com.aliyun.sdk.service.schedulerx220190430.models.GetJobInfoResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/25 15:47
 * &#064;Version 1.0
 */
public class AliyunSchedulerXClient {

    public static void run() throws Exception {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                                                                                    .accessKeyId("X")
                                                                                    .accessKeySecret("X")
                                                                                    .build());
        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(ClientOverrideConfiguration.create()
                                               // Endpoint 请参考 https://api.aliyun.com/product/schedulerx2
                                               .setEndpointOverride("schedulerx.aliyuncs.com")
                                       //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();
        GetJobInfoRequest getJobInfoRequest = GetJobInfoRequest.builder()
                .jobId(8538L)
                .groupId("finance-account-core")
                .namespace("078b1e28-df91-4b5f-a5b8-145ba96e719b")
                .regionId("eu-central-1")
                .build();

        CompletableFuture<GetJobInfoResponse> response = client.getJobInfo(getJobInfoRequest);
        // Synchronously get the return value of the API request
        GetJobInfoResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));
        client.close();
    }

}
