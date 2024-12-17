package com.baiyi.cratos.domain.channel;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import jakarta.websocket.Session;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 16:59
 * &#064;Version 1.0
 */
public interface BaseChannelHandler<T extends HasSocketRequest> extends HasTopic, InitializingBean {

    void handleRequest(String sessionId, Session session, T message) throws IllegalStateException, IOException;

    default void send(Session session, MessageResponse<?> response) throws IllegalStateException, IOException {
        session.getBasicRemote()
                .sendText(response.toString());
    }

}
