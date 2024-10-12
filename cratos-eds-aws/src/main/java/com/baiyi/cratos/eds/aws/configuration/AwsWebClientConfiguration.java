package com.baiyi.cratos.eds.aws.configuration;

import com.baiyi.cratos.eds.aws.service.Ec2InstancesService;
import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午2:46
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
public class AwsWebClientConfiguration {

    // https://tedivm.github.io/ec2details/api/ec2instances.json
    private static final String BASE_URL = "https://tedivm.github.io";

    // buffer
    private static final int BUFFER_SIZE = 16 * 1024 * 1024;

    @Bean
    public Ec2InstancesService ec2InstancesService() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        WebClient webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(codecs -> codecs.defaultCodecs()
                        .maxInMemorySize(BUFFER_SIZE))
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter)
                .build();
        return factory.createClient(Ec2InstancesService.class);
    }

}
