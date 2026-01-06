package com.baiyi.cratos.eds.opscloud.service;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsOpscloudConfigModel;
import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 11:39
 * &#064;Version 1.0
 */
public class OpscloudServiceFactory {

    private static final int MAX_IN_MEMORY_SIZE = 10 * 1024 * 1024;

    public static OpscloudService createOpscloudService(EdsConfigs.Opscloud opscloud) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .baseUrl(opscloud.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter((request, next) -> {
                        ClientRequest newRequest = ClientRequest.from(request)
                                .header("AccessToken", opscloud.getCred().getAccessToken())
                                .build();
                        return next.exchange(newRequest);
                })
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(OpscloudService.class);
    }

}
