package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.common.model.CratosHostHolder;
import com.baiyi.cratos.configuration.WebSocketConfig;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.ssh.SimpleState;
import com.baiyi.cratos.service.SshSessionService;
import com.baiyi.cratos.ssh.core.builder.SshSessionBuilder;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.enums.SshSessionTypeEnum;
import com.baiyi.cratos.ssh.core.task.crystal.SentOutputTask;
import com.baiyi.cratos.ssh.crystal.factory.SshCrystalMessageHandlerFactory;
import com.google.gson.GsonBuilder;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder.Values.WEBSOCKET_TIMEOUT;
import static com.baiyi.cratos.ssh.core.SshCrystalMessageHandler.NO_MESSAGE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/12 10:57
 * &#064;Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/socket/ssh/crystal/{username}", configurator = WebSocketConfig.class)
@Component
public class SshCrystalSocketServer {

    private SshSession sshSession;

    private static SshSessionService sshSessionService;

    // private static final HostInfo SERVER_INFO = HostInfo.build();

    private final String sessionId = UUID.randomUUID()
            .toString();

    @Autowired
    public void setSshSessionService(SshSessionService sshSessionService) {
        setSshSession(sshSessionService);
    }

    private static void setSshSession(SshSessionService sshSessionService) {
        SshCrystalSocketServer.sshSessionService = sshSessionService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        log.debug("User {} open webSocket.", username);
        // 认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, null);
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
        try {
            CratosHostHolder.CratosHost host = CratosHostHolder.get();
            log.info("Crystal ssh session try to connect: sessionId={}, hostAddress={}", sessionId,
                    host.getHostAddress());
            SshSession sshSession = SshSessionBuilder.build(sessionId, username, host, SshSessionTypeEnum.WEB_SHELL);
            sshSessionService.add(sshSession);
            this.sshSession = sshSession;
            session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
            // 启动任务 JDK21 VirtualThreads
            Thread.ofVirtual()
                    .start(new SentOutputTask(sessionId, session));
        } catch (Exception e) {
            log.error("Crystal ssh create connection error: {}", e.getMessage());
        }
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (!session.isOpen() || !StringUtils.hasText(message)) {
            return;
        }
        try {
            SimpleState ss = new GsonBuilder().create()
                    .fromJson(message, SimpleState.class);
            SshCrystalMessageHandlerFactory.getByState(ss.getState())
                    .handle(message, session, sshSession);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        SshCrystalMessageHandlerFactory.getByState(MessageState.CLOSE.name())
                .handle(NO_MESSAGE, session, sshSession);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        // TODO
    }

}
