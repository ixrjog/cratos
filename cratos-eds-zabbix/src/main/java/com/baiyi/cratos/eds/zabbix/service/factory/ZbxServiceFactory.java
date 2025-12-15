package com.baiyi.cratos.eds.zabbix.service.factory;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.service.base.BaseZbxService;
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
 * &#064;Date  2025/10/29 10:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxServiceFactory {

    private static final int MAX_IN_MEMORY_SIZE = 10 * 1024 * 1024;

    public static <T extends BaseZbxService> T createService(EdsConfigs.Zabbix zbx, Class<T> serviceClass) {
        return buildFactory(zbx, null).createClient(serviceClass);
    }

    private static HttpServiceProxyFactory buildFactory(EdsConfigs.Zabbix zbx,
                                                        java.util.function.Consumer<WebClient.Builder> customizer) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(zbx.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE));
        if (customizer != null) {
            customizer.accept(builder);
        }
        WebClient webClient = builder.build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        return HttpServiceProxyFactory.builderFor(adapter)
                .build();
    }

    public static <T extends BaseZbxService> T createAuthenticatedService(EdsConfigs.Zabbix zbx,
                                                                          Class<T> serviceClass) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        // 预先获取一次 Bean，避免在每次请求中过度查找
        ZbxTokenHolder zbxTokenHolder = SpringContextUtils.getBean(ZbxTokenHolder.class);
        WebClient webClient = WebClient.builder()
                .baseUrl(zbx.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY_SIZE))
                .filter((request, next) -> {
                    String bearer = zbxTokenHolder.getBearer(zbx);
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

}
