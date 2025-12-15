package com.baiyi.cratos.eds.harbor.service;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.eds.core.config.model.EdsHarborConfigModel;
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
 * &#064;Date  2024/11/26 10:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HarborServiceFactory {

    public static HarborService createHarborService(EdsHarborConfigModel.Harbor harbor) {
        if (harbor == null) {
            throw new IllegalArgumentException("harbor must not be null");
        }
        java.time.Duration responseTimeout = java.time.Duration.ofSeconds(30);
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(responseTimeout)
                .compress(true);
        WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl(harbor.acqUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(cfg -> cfg.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024));
        String basic = (harbor.getCred() != null) ? harbor.getCred()
                .toBasic() : null;
        webClientBuilder.defaultHeaders(headers -> {
            if (StringUtils.hasText(basic)) {
                headers.set(Global.AUTHORIZATION.toLowerCase(), basic);
            }
        });
        WebClient webClient = webClientBuilder.build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(HarborService.class);
    }

}
