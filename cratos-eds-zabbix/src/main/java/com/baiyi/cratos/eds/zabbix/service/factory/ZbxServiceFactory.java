package com.baiyi.cratos.eds.zabbix.service.factory;

import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.baiyi.cratos.eds.core.config.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.zabbix.auth.ZbxTokenHolder;
import com.baiyi.cratos.eds.zabbix.service.base.BaseZbxService;
import io.netty.channel.ChannelOption;
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
public class ZbxServiceFactory {

    public static <T extends BaseZbxService> T createService(EdsZabbixConfigModel.Zabbix zbx, Class<T> serviceClass) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(zbx.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(serviceClass);
    }

    public static <T extends BaseZbxService> T createAuthenticatedService(EdsZabbixConfigModel.Zabbix zbx, Class<T> serviceClass) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        WebClient webClient = WebClient.builder()
                .baseUrl(zbx.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024))
                .filter((request, next) -> {
                    ZbxTokenHolder zbxTokenHolder = SpringContextUtils.getBean(ZbxTokenHolder.class);
                    String bearer = zbxTokenHolder.getBearer(zbx);
                    // 在WebClient实现Zbx认证头
                    if (StringUtils.hasText(bearer)) {
                        ClientRequest newRequest = ClientRequest.from(request)
                                .header("Authorization", bearer)
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
