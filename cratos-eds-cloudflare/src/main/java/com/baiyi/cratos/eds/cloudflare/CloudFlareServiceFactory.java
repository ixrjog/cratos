package com.baiyi.cratos.eds.cloudflare;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.eds.cloudflare.converter.StringToListHttpMessageConverter;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareCertificateService;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareDnsService;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareIPsService;
import com.baiyi.cratos.eds.cloudflare.service.CloudFlareZoneService;
import com.baiyi.cratos.eds.cloudflare.service.base.CloudFlareService;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsCloudflareConfigModel;
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
 * &#064;Date  2025/12/18 14:54
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudFlareServiceFactory {

    private static final int MAX_IN_MEMORY_SIZE = 10 * 1024 * 1024;

    public static CloudFlareZoneService createZoneService(EdsConfigs.Cloudflare config) {
        return createAuthenticatedService(config, CloudFlareZoneService.class);
    }

    public static CloudFlareCertificateService createCertificateService(EdsConfigs.Cloudflare config) {
        return createAuthenticatedService(config, CloudFlareCertificateService.class);
    }

    public static CloudFlareDnsService createDnsService(EdsConfigs.Cloudflare config) {
        return createAuthenticatedService(config, CloudFlareDnsService.class);
    }

    private static <T extends CloudFlareService> T createAuthenticatedService(EdsConfigs.Cloudflare config,
                                                                              Class<T> serviceClass) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        // 预先获取一次 Bean，避免在每次请求中过度查找
        WebClient webClient = WebClient.builder()
                .baseUrl(EdsCloudflareConfigModel.CLIENT_API)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .filter((request, next) -> {
                    final String bearer = config.getCred()
                            .toBearer();
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
        return factory.createClient(serviceClass);
    }

    public static CloudFlareIPsService createIPsService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        
        WebClient webClient = WebClient.builder()
                .baseUrl("https://www.cloudflare.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(MAX_IN_MEMORY_SIZE);
                    // 添加自定义序列化器 - 将字符串转换为List<String>
                    configurer.customCodecs().register(new StringToListHttpMessageConverter());
                })
                .build();
        
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(CloudFlareIPsService.class);
    }
    
}
