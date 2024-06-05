package com.baiyi.cratos.eds.domain.configuration;

import com.baiyi.cratos.eds.domain.service.GodaddyDomainService;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:21
 * &#064;Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class GodaddyWebClientConfiguration {

    private static final String BASE_URL = "https://api.godaddy.com";

    @Bean
    public GodaddyDomainService godaddyDomainService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(GodaddyDomainService.class);
    }

}
