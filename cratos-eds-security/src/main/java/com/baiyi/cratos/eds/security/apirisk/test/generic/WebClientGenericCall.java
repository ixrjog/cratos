package com.baiyi.cratos.eds.security.apirisk.test.generic;

import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.InetAddress;
import java.net.URI;
import java.util.Map;

public class WebClientGenericCall {

    private final WebClient webClient;
    private final WebClient.Builder webClientBuilder;

    public WebClientGenericCall(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        HttpClient httpClient = HttpClient.create()
                .compress(true);
        this.webClient = webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public <T> Mono<T> genericCall(String url, HttpMethod method, Map<String, String> headers, Object requestBody,
                                   Class<T> responseType) {
        WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON);

        if (headers != null) {
            headers.forEach(requestSpec::header);
        }
        if (requestBody != null) {
            return requestSpec.bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(responseType);
        } else {
            return requestSpec.retrieve()
                    .bodyToMono(responseType);
        }
    }

    public Mono<Map<String, Object>> genericCallForMap(String url, HttpMethod method, Map<String, String> headers,
                                                       Object requestBody) {
        WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON);
        if (headers != null) {
            headers.forEach(requestSpec::header);
        }
        ParameterizedTypeReference<Map<String, Object>> typeRef = new ParameterizedTypeReference<Map<String, Object>>() {
        };
        if (requestBody != null) {
            return requestSpec.bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(typeRef);
        } else {
            return requestSpec.retrieve()
                    .bodyToMono(typeRef);
        }
    }

    public Mono<GenericCall.Response> genericCallForResponse(String url, HttpMethod method, Map<String, String> headers,
                                                             Object requestBody) {
        return genericCallForResponse(url, method, headers, requestBody, null);
    }

    public Mono<GenericCall.Response> genericCallForResponse(String url, HttpMethod method, Map<String, String> headers,
                                                             Object requestBody, String originServer) {
        WebClient client = this.webClient;
        if (originServer != null && !originServer.isEmpty()) {
            try {
                // 解析原始URL中的域名
                String originalHost = URI.create(url)
                        .getHost();
                // 解析 originServer 的 IP
                InetAddress address = InetAddress.getByName(originServer);
                String ip = address.getHostAddress();
                // 创建自定义DNS解析的HttpClient
                HttpClient httpClient = HttpClient.create()
                        .compress(true)
                        .resolver(spec -> spec.resolvedAddressTypes(io.netty.resolver.ResolvedAddressTypes.IPV4_ONLY)
                                .hostsFileEntriesResolver((host, types) -> {
                                    if (host.equalsIgnoreCase(originalHost)) {
                                        try {
                                            return InetAddress.getByName(ip);
                                        } catch (Exception e) {
                                            return null;
                                        }
                                    }
                                    return null;
                                }));
                client = webClientBuilder.clone()
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .build();
            } catch (Exception e) {
                throw new RuntimeException("Failed to resolve originServer: " + originServer, e);
            }
        }

        String finalUrl = decodeUrlIfNeeded(url);
        WebClient.RequestBodySpec requestSpec = client.method(method)
                .uri(uriBuilder -> {
                    try {
                        java.net.URL parsedUrl = new java.net.URL(finalUrl);
                        return uriBuilder
                                .scheme(parsedUrl.getProtocol())
                                .host(parsedUrl.getHost())
                                .port(parsedUrl.getPort())
                                .path(parsedUrl.getPath())
                                .query(parsedUrl.getQuery())
                                .build();
                    } catch (Exception e) {
                        return URI.create(url);
                    }
                });

        if (headers != null) {
            headers.entrySet()
                    .stream()
                    .filter(e -> !e.getKey()
                            .equalsIgnoreCase("accept-encoding"))
                    .forEach(e -> requestSpec.header(e.getKey(), e.getValue()));
        }
        requestSpec.contentType(MediaType.APPLICATION_JSON);
        WebClient.RequestHeadersSpec<?> spec = requestBody != null ? requestSpec.bodyValue(requestBody) : requestSpec;
        return spec.exchangeToMono(response -> response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> GenericCall.Response.builder()
                        .statusCode(response.statusCode()
                                            .value())
                        .headers(response.headers()
                                         .asHttpHeaders()
                                         .toSingleValueMap())
                        .body(body)
                        .build()));
    }

    public <T> Mono<T> get(String url, Class<T> responseType) {
        return genericCall(url, HttpMethod.GET, null, null, responseType);
    }

    public Mono<Map<String, Object>> getForMap(String url) {
        return genericCallForMap(url, HttpMethod.GET, null, null);
    }

    public <T> Mono<T> post(String url, Object requestBody, Class<T> responseType) {
        return genericCall(url, HttpMethod.POST, null, requestBody, responseType);
    }

    public Mono<Map<String, Object>> postForMap(String url, Object requestBody) {
        return genericCallForMap(url, HttpMethod.POST, null, requestBody);
    }

    /**
     * 如果 URL 中的 query 参数已编码（包含 %），则解码还原后再调用
     */
    private String decodeUrlIfNeeded(String url) {
        int queryIdx = url.indexOf('?');
        if (queryIdx < 0) {
            return url;
        }
        String path = url.substring(0, queryIdx);
        String query = url.substring(queryIdx + 1);
        if (query.contains("%")) {
            try {
                query = java.net.URLDecoder.decode(query, java.nio.charset.StandardCharsets.UTF_8);
            } catch (Exception ignored) {
            }
        }
        return path + "?" + query;
    }
}