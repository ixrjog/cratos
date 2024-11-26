package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.configuration.WebSocketConfig;
import com.baiyi.cratos.controller.socket.base.BaseSocketAuthenticationServer;
import com.baiyi.cratos.domain.channel.BaseChannelHandler;
import com.baiyi.cratos.domain.channel.factory.SshAuditChannelHandlerFactory;
import com.baiyi.cratos.domain.param.socket.audit.SshSessionAuditParam;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;


/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/29 下午2:45
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@Component
@ServerEndpoint(value = "/socket/ssh/audit/{username}", configurator = WebSocketConfig.class)
public class SshSessionAuditServer extends BaseSocketAuthenticationServer {

    private final String sessionId = UUID.randomUUID()
            .toString();

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (StringUtils.hasText(message)) {
            try {
                SshSessionAuditParam.AuditRequest auditRequest = SshSessionAuditParam.loadAs(message);
                BaseChannelHandler<SshSessionAuditParam.AuditRequest> handler = (BaseChannelHandler<SshSessionAuditParam.AuditRequest>) SshAuditChannelHandlerFactory.getHandler(
                        auditRequest.getTopic());
                handler.handleRequest(sessionId, session, auditRequest);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
    }

}
