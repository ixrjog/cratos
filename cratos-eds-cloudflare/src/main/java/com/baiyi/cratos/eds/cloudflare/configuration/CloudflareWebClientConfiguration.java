package com.baiyi.cratos.eds.cloudflare.configuration;

import com.baiyi.cratos.eds.cloudflare.service.CloudflareService;
import com.baiyi.cratos.eds.core.config.model.EdsCloudflareConfigModel;
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
    public CloudflareService cloudflareService() {
        HttpClient httpClient = HttpClient.create()
                // 走代理
//                .proxy(proxy ->
//                    proxy.type(ProxyProvider.Proxy.SOCKS5).address(new InetSocketAddress("10.10.10.10", 80)))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(EdsCloudflareConfigModel.CLIENT_API)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(CloudflareService.class);
    }

}
