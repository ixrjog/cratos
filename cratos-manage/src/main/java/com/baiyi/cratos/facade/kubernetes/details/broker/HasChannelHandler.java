package com.baiyi.cratos.facade.kubernetes.details.broker;

import com.baiyi.cratos.common.MessageResponse;
import com.baiyi.cratos.domain.HasTopic;
import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.baiyi.cratos.facade.kubernetes.details.KubernetesDetailsChannelHandlerFactory;
import jakarta.websocket.Session;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 16:59
 * &#064;Version 1.0
 */
public interface HasChannelHandler<T extends HasSocketRequest> extends HasTopic, InitializingBean {

    void handleRequest(String sessionId, Session session, T message) throws IOException;

    default void afterPropertiesSet() {
        KubernetesDetailsChannelHandlerFactory.register(this);
    }

    default void send(Session session, MessageResponse<?> response) {
        if (session.isOpen()) {
            // session.getBasicRemote().sendText(response.toString());
            session.getAsyncRemote()
                    .sendObject(response);
        }
    }

}
