package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.configuration.socket.MyServerEndpointConfigConfig;
import com.baiyi.cratos.controller.socket.base.BaseSocketAuthenticationServer;
import com.baiyi.cratos.domain.session.EdsKubernetesNodeDetailsRequestSession;
import com.baiyi.cratos.facade.kubernetes.node.broker.EdsKubernetesNodeDetailsBroker;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/26 13:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
@ServerEndpoint(value = "/socket/eds/kubernetes/node/{username}", configurator = MyServerEndpointConfigConfig.class)
public class EdsKubernetesNodeDetailsServer extends BaseSocketAuthenticationServer {

    public static final long WEBSOCKET_TIMEOUT = TimeUnit.MINUTES.toMillis(5);
    private final String sessionId = UUID.randomUUID()
            .toString();

    @Override
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        super.onOpen(session, username);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        // 消息代理
        Thread.ofVirtual()
                .start(EdsKubernetesNodeDetailsBroker.newBroker(this.sessionId, session));
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (StringUtils.hasText(message)) {
            try {
                com.baiyi.cratos.domain.param.socket.kubernetes.EdsKubernetesNodeParam.EdsKubernetesNodeDetailsRequest request
                        = com.baiyi.cratos.domain.param.socket.kubernetes.EdsKubernetesNodeParam.loadAs(
                        message);
                EdsKubernetesNodeDetailsRequestSession.putRequestMessage(this.sessionId, request);
            } catch (JsonSyntaxException ignored) {
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        // TODO
    }

    @OnClose
    public void onClose(Session session) {
        EdsKubernetesNodeDetailsRequestSession.remove(sessionId);
    }

}