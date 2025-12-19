package com.baiyi.cratos.eds.domain.godaddy;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.domain.godaddy.service.GodaddyDomainService;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 15:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GodaddyServiceFactory {

    private static final int MAX_IN_MEMORY_SIZE = 10 * 1024 * 1024;
    private static final String BASE_URL = "https://api.godaddy.com";

    public static GodaddyDomainService createDomainService(EdsConfigs.Godaddy config) {
        return createAuthenticatedService(config, GodaddyDomainService.class);
    }

    private static <T extends GodaddyService> T createAuthenticatedService(EdsConfigs.Godaddy config,
                                                                           Class<T> serviceClass) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        // 预先获取一次 Bean，避免在每次请求中过度查找
        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .filter((request, next) -> {
                    ClientRequest newRequest = ClientRequest.from(request)
                            .header(
                                    Global.AUTHORIZATION, config.getCred()
                                            .toSsoKey()
                            )
                            .build();
                    return next.exchange(newRequest);
                })
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(serviceClass);
    }

}
