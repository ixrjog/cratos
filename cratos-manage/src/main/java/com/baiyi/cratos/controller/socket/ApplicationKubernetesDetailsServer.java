package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.common.util.SshIdUtils;
import com.baiyi.cratos.configuration.socket.MyServerEndpointConfigConfig;
import com.baiyi.cratos.controller.socket.base.BaseSocketAuthenticationServer;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.session.KubernetesDetailsRequestSession;
import com.baiyi.cratos.facade.kubernetes.details.broker.ApplicationKubernetesDetailsBroker;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.EOFException;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 13:54
 * &#064;Version 1.0
 */
@Slf4j
@Component
@ServerEndpoint(value = "/socket/application/kubernetes/details/{username}", configurator = MyServerEndpointConfigConfig.class)
public class ApplicationKubernetesDetailsServer extends BaseSocketAuthenticationServer {

    public static final long WEBSOCKET_TIMEOUT = TimeUnit.MINUTES.toMillis(5);
    private final String sessionId = SshIdUtils.generateID();

    @Override
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        super.onOpen(session, username);
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        // 获取当前的 SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();
        // 消息代理
        Thread.ofVirtual()
                .start(ApplicationKubernetesDetailsBroker.newBroker(this.sessionId, session, context));
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (StringUtils.hasText(message)) {
            try {
                ApplicationKubernetesParam.KubernetesDetailsRequest request = ApplicationKubernetesParam.loadAs(
                        message);
                KubernetesDetailsRequestSession.putRequestMessage(this.sessionId, request);
            } catch (JsonSyntaxException ex) {
                log.warn("Invalid JSON message received: {}", message, ex);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (error instanceof EOFException) {
            log.info("Application kubernetes details webSocket connection closed by client: {}", session.getId());
            // 清理资源
            connectionTerminated(session);
        } else {
            log.error("Application kubernetes details webSocket error", error);
        }
    }

    @OnClose
    public void onClose(Session session) {
        connectionTerminated(session);
    }

    private void connectionTerminated(Session session) {
        KubernetesDetailsRequestSession.remove(sessionId);
    }

}