package com.baiyi.cratos.eds.secutity.apirisk.service;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsApiRiskConfigModel;
import io.netty.channel.ChannelOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:19
 * &#064;Version 1.0
 */
@SuppressWarnings("UastIncorrectHttpHeaderInspection")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRiskServiceFactory {

    private static final String SIGN_TYPE = "md5";
    private static final String HEADER_SIGN_TYPE = "audit-open-api-signType";
    private static final String HEADER_ACCESS_KEY = "audit-open-api-accessKey";
    private static final String HEADER_SIGN_VALUE = "audit-open-api-signValue";
    private static final String HEADER_TIMESTAMP = "audit-open-api-timestamp";

    public static ApiRiskService createApiRiskService(EdsConfigs.ApiRisk apiRisk) {
        if (apiRisk == null) {
            throw new IllegalArgumentException("apiRisk must not be null");
        }
        EdsApiRiskConfigModel.Cred cred = apiRisk.getCred();
        java.time.Duration responseTimeout = java.time.Duration.ofSeconds(30);
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(responseTimeout)
                .compress(true);
        WebClient webClient = WebClient.builder()
                .baseUrl(apiRisk.getUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .defaultHeaders(headers -> {
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    String signValue = sign(cred.getAccessKey(), cred.getSecretKey(), timestamp);
                    headers.set(HEADER_SIGN_TYPE, SIGN_TYPE);
                    headers.set(HEADER_ACCESS_KEY, cred.getAccessKey());
                    headers.set(HEADER_SIGN_VALUE, signValue);
                    headers.set(HEADER_TIMESTAMP, timestamp);
                })
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ApiRiskService.class);
    }

    /**
     * 签名算法
     * 1. 拼接: signType=md5&accessKey={ak}&secretKey={sk}&timestamp={ts}
     * 2. MD5 后转大写
     */
    public static String sign(String accessKey, String secretKey, String timestamp) {
        String raw = "signType=" + SIGN_TYPE
                + "&accessKey=" + accessKey
                + "&secretKey=" + secretKey
                + "&timestamp=" + timestamp;
        return md5(raw);
    }

    private static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

}
