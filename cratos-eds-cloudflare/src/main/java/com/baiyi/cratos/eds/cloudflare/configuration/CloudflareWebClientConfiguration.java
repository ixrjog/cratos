package com.baiyi.cratos.eds.cloudflare.configuration;

import com.baiyi.cratos.eds.cloudflare.service.CloudflareCertService;
import com.baiyi.cratos.eds.cloudflare.service.CloudflareZoneService;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
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
 * @Date 2024/3/1 16:56
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class CloudflareWebClientConfiguration {

    @Bean
    public CloudflareZoneService cloudflareZoneService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        WebClient webClient = WebClient.builder()
                .baseUrl(EdsCloudflareConfigModel.CLIENT_API)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(CloudflareZoneService.class);
    }

    @Bean
    public CloudflareCertService cloudflareCertService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        WebClient webClient = WebClient.builder()
                .baseUrl(EdsCloudflareConfigModel.CLIENT_API)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(CloudflareCertService.class);
    }

}
