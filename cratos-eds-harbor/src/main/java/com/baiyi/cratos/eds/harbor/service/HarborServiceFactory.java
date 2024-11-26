package com.baiyi.cratos.eds.harbor.service;

import com.baiyi.cratos.eds.core.config.EdsHarborConfigModel;
import io.netty.channel.ChannelOption;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 10:48
 * &#064;Version 1.0
 */
public class HarborServiceFactory {

    public static HarborService createHarborService(EdsHarborConfigModel.Harbor harbor) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(harbor.acqUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(HarborService.class);
    }

}
