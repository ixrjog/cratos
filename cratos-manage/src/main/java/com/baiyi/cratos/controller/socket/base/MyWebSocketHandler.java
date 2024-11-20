package com.baiyi.cratos.controller.socket.base;

import jakarta.websocket.PongMessage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 15:45
 * &#064;Version 1.0
 */
@Slf4j
public class MyWebSocketHandler extends AbstractWebSocketHandler {

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {
        if (message instanceof PongMessage) {
            log.info("收到 Pong");
        }
    }

}
