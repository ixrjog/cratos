package com.baiyi.cratos.eds.security.apirisk.test.generic;

import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GenericCallService {
    
    private final WebClientGenericCall genericCall;
    
    public GenericCallService(WebClient.Builder webClientBuilder) {
        this.genericCall = new WebClientGenericCall(webClientBuilder);
    }
    
    public Mono<Map<String, Object>> callDynamicApi(String apiUrl, String method, 
                                                    Map<String, Object> params) {
        return fullyDynamicCallForMap(apiUrl, method, null, params);
    }

    public Mono<GenericCall.Response> callDynamicApiWithResponse(String apiUrl, String method,
                                                                 Map<String, String> headers,
                                                                 String body,
                                                                 String originServer) {
        return genericCall.genericCallForResponse(
                apiUrl,
                org.springframework.http.HttpMethod.valueOf(method.toUpperCase()),
                headers,
                body,
                originServer
        );
    }
    
    public Mono<Map<String, Object>> fullyDynamicCallForMap(String url, String httpMethod,
                                                           Map<String, String> headers,
                                                           Object body) {
        return genericCall.genericCallForMap(
            url,
            org.springframework.http.HttpMethod.valueOf(httpMethod.toUpperCase()),
            headers,
            body
        );
    }
    
    public <T> Mono<T> fullyDynamicCall(String url, String httpMethod,
                                        Map<String, String> headers,
                                        Object body,
                                        Class<T> responseType) {
        return genericCall.genericCall(
            url,
            org.springframework.http.HttpMethod.valueOf(httpMethod.toUpperCase()),
            headers,
            body,
            responseType
        );
    }

}