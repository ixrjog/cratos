package com.baiyi.cratos.eds.crt.config;

import com.baiyi.cratos.eds.crt.service.CrtShService;
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
 * &#064;Date  2026/1/14 18:10
 * &#064;Version 1.0
 */
@Configuration
public class CrtShServiceConfig {

    @Bean
    public CrtShService createCrtShService() {
        java.time.Duration responseTimeout = java.time.Duration.ofSeconds(30);
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(responseTimeout)
                .compress(true);
        WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl("https://crt.sh")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(cfg -> cfg.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024));
        WebClient webClient = webClientBuilder.build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(CrtShService.class);
    }

}
