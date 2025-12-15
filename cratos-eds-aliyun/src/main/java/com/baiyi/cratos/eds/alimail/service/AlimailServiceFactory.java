package com.baiyi.cratos.eds.alimail.service;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.baiyi.cratos.eds.alimail.auth.AlimailTokenHolder;
import com.baiyi.cratos.eds.core.config.model.EdsAlimailConfigModel;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:40
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlimailServiceFactory {

    private static final int MAX_IN_MEMORY_SIZE = 10 * 1024 * 1024;

    private static AlimailService createAlimailService(EdsAlimailConfigModel.Alimail alimail) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(alimail.getApi()
                                 .getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(AlimailService.class);
    }

    public static AlimailService createAuthenticatedService(EdsAlimailConfigModel.Alimail alimail) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        // 预先获取一次 Bean，避免在每次请求中过度查找
        AlimailTokenHolder alimailTokenHolder = SpringContextUtils.getBean(AlimailTokenHolder.class);
        WebClient webClient = WebClient.builder()
                .baseUrl(alimail.getApi()
                                 .getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .filter((request, next) -> {
                    String bearer = alimailTokenHolder.getBearer(alimail);
                    if (StringUtils.hasText(bearer)) {
                        ClientRequest newRequest = ClientRequest.from(request)
                                .header(Global.AUTHORIZATION, bearer)
                                .build();
                        return next.exchange(newRequest);
                    }
                    return next.exchange(request);
                })
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(AlimailService.class);
    }

}
