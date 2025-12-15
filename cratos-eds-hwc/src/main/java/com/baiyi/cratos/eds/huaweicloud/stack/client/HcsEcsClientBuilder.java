package com.baiyi.cratos.eds.huaweicloud.stack.client;

import com.baiyi.cratos.eds.core.config.model.EdsHcsConfigModel;
import com.huaweicloud.sdk.core.HttpListener;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/28 15:54
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HcsEcsClientBuilder {

    public static EcsClient buildEcsClient(EdsHcsConfigModel.Hcs hcStack) {
        // 注册监听器后打印原始请求信息,请勿用于生产环境
        HttpListener requestListener = HttpListener.forRequestListener(listener ->
                System.out.printf("> Request %s %s\n> Headers:\n%s\n> Body: %s\n",
                        listener.httpMethod(),
                        listener.uri(),
                        listener.headers().entrySet().stream()
                                .flatMap(entry -> entry.getValue().stream().map(
                                        value -> "\t" + entry.getKey() + ": " + value))
                                .collect(Collectors.joining("\n")),
                        listener.body().orElse("")));
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.addHttpListener(requestListener);
        config.withIgnoreSSLVerification(true);
        Region manageOne = new Region("axentec-jashore-1",
                hcStack.getManageOne().getConsoleUrl());
        // 创建认证
        BasicCredentials credentials = new BasicCredentials().withAk(hcStack.getCred()
                        .getAccessKey())
                .withSk(hcStack.getCred()
                        .getSecretKey())
                .withProjectId("c03121b299c743be944f3877093f8a37");

        return EcsClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(credentials)
                .withRegion(manageOne)
                .build();
    }

}
