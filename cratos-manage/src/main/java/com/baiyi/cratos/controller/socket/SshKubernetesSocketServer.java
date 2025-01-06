package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.common.model.CratosHostHolder;
import com.baiyi.cratos.configuration.socket.MyServerEndpointConfigConfig;
import com.baiyi.cratos.controller.socket.base.BaseSocketAuthenticationServer;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.socket.kubernetes.ApplicationKubernetesParam;
import com.baiyi.cratos.domain.session.KubernetesDetailsRequestSession;
import com.baiyi.cratos.service.SshSessionService;
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

import java.util.UUID;
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

    private SshSession sshSession;
    private static SshSessionService sshSessionService;
    public static final long WEBSOCKET_TIMEOUT = TimeUnit.MINUTES.toMillis(15);
    private final String sessionId = UUID.randomUUID()
            .toString();

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
        session.setMaxIdleTimeout(WEBSOCKET_TIMEOUT);
        try {
            CratosHostHolder.CratosHost host = CratosHostHolder.get();
            log.info("Kubernetes ssh session try to connect: sessionId={}, hostAddress={}", sessionId,
                    host.getHostAddress());
            SshSession sshSession = SshSessionBuilder.build(sessionId, username, host, SshSessionTypeEnum.WEB_KUBERNETES_SHELL);
            SshKubernetesSocketServer.sshSessionService.add(sshSession);
            this.sshSession = sshSession;
            // 启动任务 JDK21 VirtualThreads
            Thread.ofVirtual()
                    .start(SentOutputTask.newTask(sessionId, session));
        } catch (Exception e) {
            log.error("Kubernetes ssh create connection error: {}", e.getMessage());
        }
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (StringUtils.hasText(message)) {
            try {
                ApplicationKubernetesParam.KubernetesDetailsRequest kubernetesDetailsRequest = ApplicationKubernetesParam.loadAs(
                        message);
               // KubernetesDetailsRequestSession.putRequestMessage(this.sessionId, kubernetesDetailsRequest);
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
        KubernetesDetailsRequestSession.remove(sessionId);
    }

}