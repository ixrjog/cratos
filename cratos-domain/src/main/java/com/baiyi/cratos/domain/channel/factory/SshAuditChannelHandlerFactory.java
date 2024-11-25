package com.baiyi.cratos.domain.channel.factory;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/25 15:12
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class SshAuditChannelHandlerFactory {

    /**
     * Map<Topic, HasChannelHandler>
     */
    private static final Map<String, BaseChannelHandler<? extends HasSocketRequest>> CONTEXT = new ConcurrentHashMap<>();

    public static <T extends HasSocketRequest> void register(BaseChannelHandler<T> bean) {
        CONTEXT.put(bean.getTopic(), bean);
    }

    public static BaseChannelHandler<? extends HasSocketRequest> getHandler(
            String topic) throws IllegalArgumentException {
        if (CONTEXT.containsKey(topic)) {
            return CONTEXT.get(topic);
        }
        throw new IllegalArgumentException("Topic not found.");
    }

}