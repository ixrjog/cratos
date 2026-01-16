package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.common.model.CratosHostHolder;
import com.baiyi.cratos.common.util.SshIdUtils;
import com.baiyi.cratos.configuration.socket.MyServerEndpointConfigConfig;
import com.baiyi.cratos.controller.socket.base.BaseSocketAuthenticationServer;
import com.baiyi.cratos.domain.channel.factory.KubernetesSshChannelHandlerFactory;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.socket.kubernetes.KubernetesContainerTerminalParam;
import com.baiyi.cratos.service.session.SshSessionService;
import com.baiyi.cratos.ssh.core.builder.SshSessionBuilder;
import com.baiyi.cratos.ssh.core.enums.SshSessionTypeEnum;
import com.baiyi.cratos.ssh.core.task.crystal.SentOutputTask;
import com.google.gson.JsonSyntaxException;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.EOFException;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/18 10:06
 * &#064;Version 1.0
 */
@Slf4j
@Component
@ServerEndpoint(value = "/socket/ssh/kubernetes/{username}", configurator = MyServerEndpointConfigConfig.class)
public class SshKubernetesSocketServer extends BaseSocketAuthenticationServer {

    private static SshSessionService sshSessionService;
    public static final long WEBSOCKET_TIMEOUT = TimeUnit.MINUTES.toMillis(15);
    private final String sessionId = SshIdUtils.generateID();
    private String username;

    @Autowired
    public void setSshSessionService(SshSessionService sshSessionService) {
        setSshSession(sshSessionService);
    }

    private static void setSshSession(SshSessionService sshSessionService) {
        SshKubernetesSocketServer.sshSessionService = sshSessionService;
    }

    @Override
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        super.onOpen(session, username);
        this.username = username;
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        try {
            CratosHostHolder.CratosHost host = CratosHostHolder.get();
            log.info(
                    "Kubernetes ssh session try to connect: sessionId={}, hostAddress={}", this.sessionId,
                    host.getHostAddress()
            );
            SshSession sshSession = SshSessionBuilder.build(
                    this.sessionId, username, host,
                    SshSessionTypeEnum.WEB_KUBERNETES_SHELL
            );
            SshKubernetesSocketServer.sshSessionService.add(sshSession);
            // this.sshSession = sshSession;
            Thread.ofVirtual()
                    .start(SentOutputTask.newTask(this.sessionId, session));
        } catch (Exception e) {
            log.error("Kubernetes ssh create connection error: {}", e.getMessage());
        }
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (StringUtils.hasText(message)) {
            try {
                KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest request = KubernetesContainerTerminalParam.loadAs(
                        message);
                KubernetesSshChannelHandlerFactory.handleRequest(this.sessionId, this.username, session, request);
            } catch (JsonSyntaxException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        if (error instanceof EOFException) {
            log.info("WebSocket connection closed by client: {}", session.getId());
            // 清理资源
            connectionTerminated(session);
        } else {
            log.error("WebSocket error", error);
        }
    }

    @OnClose
    public void onClose(Session session) {
        connectionTerminated(session);
    }

    private void connectionTerminated(Session session) {
        KubernetesSshChannelHandlerFactory.handleRequest(
                this.sessionId, this.username, session,
                KubernetesContainerTerminalParam.KubernetesContainerTerminalRequest.CLOSE
        );
    }

}