package com.baiyi.cratos.eds.cloudflare.client;

import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:56
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class CloudflareWebClientConfiguration {

    @Bean
    public CloudflareService cloudflareClient() {
        WebClient webClient = WebClient.builder().baseUrl(EdsCloudflareConfigModel.CLIENT_API)
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(CloudflareService.class);
    }

}
