package com.baiyi.cratos.eds.sre.bridge.service;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 14:59
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SreBridgeServiceFactory {

    public static SreBridgeService createSreBridgeService(EdsConfigs.SreEventBridge sreEventBridge) {
        if (sreEventBridge == null) {
            throw new IllegalArgumentException("Eds config sreEventBridge must not be null");
        }
        java.time.Duration responseTimeout = java.time.Duration.ofSeconds(30);
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(responseTimeout)
                .compress(true);
        WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl(sreEventBridge.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(cfg -> cfg.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024));
        String bearer = (sreEventBridge.getCred() != null) ? sreEventBridge.getCred()
                .toBearer() : null;
        webClientBuilder.defaultHeaders(headers -> {
            if (StringUtils.hasText(bearer)) {
                headers.set(Global.AUTHORIZATION.toLowerCase(), bearer);
            }
        });
        WebClient webClient = webClientBuilder.build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(SreBridgeService.class);
    }

}