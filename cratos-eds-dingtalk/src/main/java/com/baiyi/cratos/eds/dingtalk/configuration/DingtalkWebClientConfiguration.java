package com.baiyi.cratos.eds.dingtalk.configuration;

import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * @Author baiyi
 * @Date 2024/5/6 上午10:42
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DingtalkWebClientConfiguration {

    private static final String BASE_URL = "https://oapi.dingtalk.com";

    @Bean
    public DingtalkService dingtalkService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(DingtalkService.class);
    }

}
