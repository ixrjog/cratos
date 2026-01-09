package com.baiyi.cratos.eds.cloudflare.converter;

import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义HTTP消息转换器：将字符串转换为List<String>
 * 
 * @Author baiyi
 * @Date 2026/1/9 14:17
 * @Version 1.0
 */
public class StringToListHttpMessageConverter implements HttpMessageReader<List<String>> {

    @Override
    public List<MediaType> getReadableMediaTypes() {
        return Collections.singletonList(MediaType.TEXT_PLAIN);
    }

    @Override
    public boolean canRead(ResolvableType elementType, MediaType mediaType) {
        return List.class.isAssignableFrom(elementType.toClass()) && 
               MediaType.TEXT_PLAIN.isCompatibleWith(mediaType);
    }

    @Override
    public Flux<List<String>> read(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return readMono(elementType, message, hints).flux();
    }

    @Override
    public Mono<List<String>> readMono(ResolvableType elementType, ReactiveHttpInputMessage message, Map<String, Object> hints) {
        return message.getBody()
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    return new String(bytes);
                })
                .reduce(String::concat)
                .map(this::parseToList);
    }

    private List<String> parseToList(String content) {
        if (!StringUtils.hasText(content)) {
            return Collections.emptyList();
        }
        
        return Arrays.stream(content.split("\\n"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

}
