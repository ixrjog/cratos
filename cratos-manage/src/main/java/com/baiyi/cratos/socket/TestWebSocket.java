package com.baiyi.cratos.socket;

import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/12 16:33
 * @Version 1.0
 */
@Component
@ServerEndpoint("/ws/test")
public class TestWebSocket {
}
