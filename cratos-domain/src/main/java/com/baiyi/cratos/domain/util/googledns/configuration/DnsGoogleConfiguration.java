package com.baiyi.cratos.domain.util.googledns.configuration;

import com.baiyi.cratos.domain.util.googledns.service.DnsGoogleService;
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
 * &#064;Date  2025/1/16 14:16
 * &#064;Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class DnsGoogleConfiguration {

    // https://dns.google/resolve?name=www.google.com&type=CNAME
    private static final String DNS_GOOGLE_URL = "https://dns.google";

    @Bean
    public DnsGoogleService googleDnsService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(DNS_GOOGLE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(DnsGoogleService.class);
    }

}
