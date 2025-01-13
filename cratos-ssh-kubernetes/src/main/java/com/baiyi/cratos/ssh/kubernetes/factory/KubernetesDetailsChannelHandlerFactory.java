package com.baiyi.cratos.ssh.kubernetes.factory;

import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import jakarta.websocket.Session;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 16:52
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class KubernetesDetailsChannelHandlerFactory {

    /**
     * Map<Topic, HasChannelHandler>
     */
    private static final Map<String, BaseChannelHandler<? extends HasSocketRequest>> CONTEXT = new ConcurrentHashMap<>();

    public static <T extends HasSocketRequest> void register(BaseChannelHandler<T> bean) {
        CONTEXT.put(bean.getTopic(), bean);
    }

    public static <T extends HasSocketRequest> void handleRequest(String sessionId, Session session, T message) {
        try {
            if (CONTEXT.containsKey(message.getTopic())) {
                BaseChannelHandler<HasSocketRequest> handler = (BaseChannelHandler<HasSocketRequest>) CONTEXT.get(
                        message.getTopic());
                handler.handleRequest(sessionId, session, message);
            }
        } catch (IllegalStateException | ClassCastException | IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}